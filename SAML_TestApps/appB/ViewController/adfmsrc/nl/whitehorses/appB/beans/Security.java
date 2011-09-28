package nl.whitehorses.appB.beans;

import java.security.Principal;
import java.util.Set;
import javax.security.auth.Subject;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

public class Security {
    public Security() {

      System.out.println("init");
      Subject subject = weblogic.security.Security.getCurrentSubject();
      Set<Principal> allPrincipals = subject.getPrincipals();
      for (Principal principal : allPrincipals) {
        System.out.println("class: "+principal.getClass().getName());
        if ( principal instanceof WLSUserImpl ) {
           System.out.println("public found user: "+principal.getName());
           this.userName = principal.getName();
        } else if ( principal instanceof WLSGroupImpl ) {
          System.out.println("public group user: "+principal.getName());
          if ( this.roles == null ) {
            this.roles = principal.getName();
          } else {
            this.roles = this.roles + ","+ principal.getName();
          }
        }
      }

      Set<Object> allPrivatePrincipals = subject.getPrivateCredentials();
      for (Object principal : allPrivatePrincipals) {
        System.out.println("private "+principal.getClass().getName());   
      }    

    }

    private String userName;
    private String roles;
    private String attributes;

    public String getUserName() {
        System.out.println("getUser");
        return userName;
    }

    public String getRoles() {
        return roles;
    }

    public String getAttributes() {
        return attributes;
    }
}
