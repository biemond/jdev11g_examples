package nl.amis.security.opss;

import java.util.Date;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

import oracle.security.idm.IMException;
import oracle.security.idm.IdentityStore;
import oracle.security.idm.ModProperty;
import oracle.security.idm.Property;
import oracle.security.idm.PropertySet;
import oracle.security.idm.Role;
import oracle.security.idm.RoleProfile;
import oracle.security.idm.SearchParameters;
import oracle.security.idm.SearchResponse;
import oracle.security.idm.SimpleSearchFilter;
import oracle.security.idm.User;
import oracle.security.idm.UserProfile;
import oracle.security.jps.JpsContext;
import oracle.security.jps.JpsContextFactory;
import oracle.security.jps.JpsException;
import oracle.security.jps.service.credstore.CredentialStore;
import oracle.security.jps.service.credstore.PasswordCredential;
import oracle.security.jps.service.idstore.IdentityStoreService;

public class OpssBean {

    private JpsContext jpsCtx = null;
    private IdentityStore idStore = null;
    private UserProfile userProfile = null;

    private String oldPassword = null;
    private String newPassword = null;
    
    private String username = "";
    private String roles = "";
    private String attributes = "";

    private String key = null;
    private String map = null;

    private String keyUsername = null;
    private String keyPassword = null;

    private String createRole = null;

    private String createUser = null;
    private String createUserPassword = null;
    private String createUserRole = null;

    private String searchUser = null;
    private String searchUserResult = "";

    private static final String ldapAccountExpiresAttribute = "ACCOUNTEXPIRES";
    private static final String ldapLastLogonAttribute = "LASTLOGONTIMESTAMP";
    private static final String ldapPwdLastSetAttribute = "PWDLASTSET";

    public OpssBean() {

        ADFContext adfCtx = ADFContext.getCurrent();
        SecurityContext secCntx = adfCtx.getSecurityContext();

        this.username = secCntx.getUserName();

        for (String role : secCntx.getUserRoles()) {
            this.roles = this.roles + role + ", ";
        }

        try {
            jpsCtx = JpsContextFactory.getContextFactory().getContext();
            IdentityStoreService service =
                  jpsCtx.getServiceInstance(IdentityStoreService.class);
            
            idStore = service.getIdmStore();

            User user = idStore.searchUser(secCntx.getUserName());
            if (user != null) {
                userProfile = user.getUserProfile();
                PropertySet propSet = userProfile.getAllUserProperties();

                Iterator it = propSet.getAll();
                while (it.hasNext()) {
                    Property prop = (Property)it.next();
                    this.attributes =
                            this.attributes + "property: " + prop.getName();
                    Iterator it2 = prop.getValues().iterator();
                    while (it2.hasNext()) {
                        Object val = it2.next();
                        if ( prop.getName().equalsIgnoreCase(ldapAccountExpiresAttribute) ||
                             prop.getName().equalsIgnoreCase(ldapLastLogonAttribute) ||
                             prop.getName().equalsIgnoreCase(ldapPwdLastSetAttribute) ){
                        
                            long adTime = Long.parseLong(val.toString());
                            long javaTime = adTime - 0x19db1ded53e8000L;
                            javaTime /= 10000L;
                            Date day = new Date(javaTime);
                                          
                            this.attributes = this.attributes + " values: " + day.toString() + "\n";
                       } else {
                              this.attributes = this.attributes + " values: " + val.toString() + "\n";
                       }  
                    }
                }
            }
        } catch (JpsException e) {
            e.printStackTrace();
        } catch (IMException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(ActionEvent actionEvent) {
        // Add event code here...
        if ( oldPassword == null || oldPassword.equals("") ) {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                      "Old password is empty", ""); 
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return;
           }
        
              if ( newPassword == null || newPassword.equals("") ) {
                  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                      "New password is empty", ""); 
                  FacesContext.getCurrentInstance().addMessage(null, msg);
                  return;
              }
        
              if ( userProfile != null ) {
        
                  try {
                      char[] adEncodedPassword = oldPassword.toCharArray();
                      char[] adEncodedPassword2 = newPassword.toCharArray();
                     
                      userProfile.setPassword(adEncodedPassword, adEncodedPassword2);
                      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                          "Password has been changed", ""); 
                      FacesContext.getCurrentInstance().addMessage(null, msg);  
                  } catch (IMException e) {
                      e.printStackTrace();
                      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          e.getMessage(), ""); 
                      FacesContext.getCurrentInstance().addMessage(null, msg);  
                  }
              } else {
                  System.out.println("user is null");
              }

    }


//  -Djps.auth.debug=true -Djps.auth.debug.verbose=true
//
//  start wlst.cmd from oracle_common\common\bin
//
//  connect('weblogic','weblogic1','t3://localhost:7101')
//  createCred(map="JPS",key="AD_ldap",user="CN=Administrator,CN=Users,DC=alfa,DC=local",password="Welcome02" ,desc="Windows LDAP user")
//  exit()

    public void retrievePassword(ActionEvent actionEvent) {
      try {
          CredentialStore store = jpsCtx.getServiceInstance(CredentialStore.class);
          PasswordCredential password =
              (PasswordCredential)store.getCredential(this.getMap(), 
                                                      this.getKey());
          this.keyUsername = password.getName();
          this.keyPassword = new String(password.getPassword());

        FacesMessage fm = new FacesMessage("Succes");
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, fm);


      } catch (JpsException e) {
        e.printStackTrace();
        FacesMessage fm = new FacesMessage(e.getMessage());
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, fm);
      }
    }

    public void createRole(ActionEvent actionEvent) {
        try {
          idStore.getRoleManager().createRole(this.createRole);

          FacesMessage fm = new FacesMessage("Succes");
          fm.setSeverity(FacesMessage.SEVERITY_INFO);
          FacesContext context = FacesContext.getCurrentInstance();
          context.addMessage(null, fm);


        } catch (IMException e) {
          e.printStackTrace();
          FacesMessage fm = new FacesMessage(e.getMessage());
          fm.setSeverity(FacesMessage.SEVERITY_ERROR);
          FacesContext context = FacesContext.getCurrentInstance();
          context.addMessage(null, fm);
        }

    }

    public void createUser(ActionEvent actionEvent) {
      try {
          PropertySet propSet = new PropertySet();

          Property prop = new Property("samaccountname",this.createUser);
  //        Property prop2 = new Property("userAccountControl",66048);
          propSet.put(prop);
  //        propSet.put(prop2);
           
          User newUser = idStore.getUserManager()
                .createUser(this.createUser, 
                            this.createUserPassword.toCharArray(),
                            propSet);
          if ( createUserRole != null  ) {
              SimpleSearchFilter filter =
                        idStore.getSimpleSearchFilter(RoleProfile.NAME,
                                                      SimpleSearchFilter.TYPE_EQUAL,
                                                      this.createUserRole);
              SearchParameters sp = new SearchParameters(filter,
                                                         SearchParameters.SEARCH_ROLES_ONLY);
              SearchResponse response = idStore.search(sp);
              if (response.hasNext()) {
                Role role = (Role)response.next();
                idStore.getRoleManager().grantRole(role, newUser.getPrincipal());
              }
          }
          ModProperty userAccountControl = 
              new ModProperty("userAccountControl", 
                              "66048",
                              ModProperty.REPLACE );
          
          newUser.getUserProfile().setProperty(userAccountControl);
          
          FacesMessage fm = new FacesMessage("Success");
          fm.setSeverity(FacesMessage.SEVERITY_INFO);
          FacesContext context = FacesContext.getCurrentInstance();
          context.addMessage(null, fm);

      } catch (IMException e) {
          e.printStackTrace();
          FacesMessage fm = new FacesMessage(e.getMessage());
          fm.setSeverity(FacesMessage.SEVERITY_ERROR);
          FacesContext context = FacesContext.getCurrentInstance();
          context.addMessage(null, fm);
      }

    }

    public void searchUser(ActionEvent actionEvent) {
     SimpleSearchFilter filter =
           idStore.getSimpleSearchFilter(UserProfile.NAME,
                                         SimpleSearchFilter.TYPE_EQUAL,
                                         this.searchUser);
          SearchParameters sp = new SearchParameters(filter,
                                                     SearchParameters.SEARCH_USERS_ONLY);
          try {
            SearchResponse response = idStore.search(sp);
            if (response.hasNext()) {
              User user = (User)response.next();
              if (user != null) {
                UserProfile userProfile = user.getUserProfile();
                this.searchUserResult = userProfile.toString();
              }
            }  
          } catch (IMException e) {
                e.printStackTrace();
                FacesMessage fm = new FacesMessage(e.getMessage());
                fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, fm);
         }
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }


    public void setKeyUsername(String keyUsername) {
        this.keyUsername = keyUsername;
    }

    public String getKeyUsername() {
        return keyUsername;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setCreateRole(String createRole) {
        this.createRole = createRole;
    }

    public String getCreateRole() {
        return createRole;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUserPassword(String createUserPassword) {
        this.createUserPassword = createUserPassword;
    }

    public String getCreateUserPassword() {
        return createUserPassword;
    }

    public void setCreateUserRole(String createUserRole) {
        this.createUserRole = createUserRole;
    }

    public String getCreateUserRole() {
        return createUserRole;
    }

    public void setSearchUser(String searchUser) {
        this.searchUser = searchUser;
    }

    public String getSearchUser() {
        return searchUser;
    }

    public void setSearchUserResult(String searchUserResult) {
        this.searchUserResult = searchUserResult;
    }

    public String getSearchUserResult() {
        return searchUserResult;
    }


    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
