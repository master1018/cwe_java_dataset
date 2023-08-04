import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

public class RegistrationForm extends ValidatorForm {
    private String name;
    private String email;
    // Other fields

    public RegistrationForm() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        // Validation logic here
    }

    // Other methods and code
}
