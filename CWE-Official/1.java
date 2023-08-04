public class RegistrationForm extends org.apache.struts.validator.ValidatorForm {
private String name;
private String email;
    public RegistrationForm() {
        super();
    }
public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
ActionErrors errors = new ActionErrors();
if (getName() == null || getName().length() < 1) {
errors.add("name", new ActionMessage("error.name.required"));
}
return errors;
}
}
