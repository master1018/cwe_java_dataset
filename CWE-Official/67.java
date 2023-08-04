public class RegistrationForm extends org.apache.struts.validator.ValidatorForm {
public String name;
public String email;
...
public RegistrationForm() {
super();
}
public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {...}
...
}
