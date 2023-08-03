
package net.sf.mpxj.ganttproject;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import net.sf.mpxj.ChildTaskContainer;
import net.sf.mpxj.ConstraintType;
import net.sf.mpxj.CustomField;
import net.sf.mpxj.Day;
import net.sf.mpxj.Duration;
import net.sf.mpxj.EventManager;
import net.sf.mpxj.FieldType;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.Priority;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarException;
import net.sf.mpxj.ProjectCalendarHours;
import net.sf.mpxj.ProjectCalendarWeek;
import net.sf.mpxj.ProjectConfig;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectProperties;
import net.sf.mpxj.Rate;
import net.sf.mpxj.Relation;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.ResourceField;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.common.DateHelper;
import net.sf.mpxj.common.NumberHelper;
import net.sf.mpxj.common.Pair;
import net.sf.mpxj.common.ResourceFieldLists;
import net.sf.mpxj.common.TaskFieldLists;
import net.sf.mpxj.common.UnmarshalHelper;
import net.sf.mpxj.ganttproject.schema.Allocation;
import net.sf.mpxj.ganttproject.schema.Allocations;
import net.sf.mpxj.ganttproject.schema.Calendars;
import net.sf.mpxj.ganttproject.schema.CustomPropertyDefinition;
import net.sf.mpxj.ganttproject.schema.CustomResourceProperty;
import net.sf.mpxj.ganttproject.schema.CustomTaskProperty;
import net.sf.mpxj.ganttproject.schema.DayTypes;
import net.sf.mpxj.ganttproject.schema.DefaultWeek;
import net.sf.mpxj.ganttproject.schema.Depend;
import net.sf.mpxj.ganttproject.schema.Project;
import net.sf.mpxj.ganttproject.schema.Resources;
import net.sf.mpxj.ganttproject.schema.Role;
import net.sf.mpxj.ganttproject.schema.Roles;
import net.sf.mpxj.ganttproject.schema.Taskproperty;
import net.sf.mpxj.ganttproject.schema.Tasks;
import net.sf.mpxj.listener.ProjectListener;
import net.sf.mpxj.reader.AbstractProjectReader;
public final class GanttProjectReader extends AbstractProjectReader
{
   @Override public void addProjectListener(ProjectListener listener)
   {
      if (m_projectListeners == null)
      {
         m_projectListeners = new ArrayList<>();
      }
      m_projectListeners.add(listener);
   }
   @Override public ProjectFile read(InputStream stream) throws MPXJException
   {
      try
      {
         if (CONTEXT == null)
         {
            throw CONTEXT_EXCEPTION;
         }
         m_projectFile = new ProjectFile();
         m_eventManager = m_projectFile.getEventManager();
         m_resourcePropertyDefinitions = new HashMap<>();
         m_taskPropertyDefinitions = new HashMap<>();
         m_roleDefinitions = new HashMap<>();
         m_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
         ProjectConfig config = m_projectFile.getProjectConfig();
         config.setAutoResourceUniqueID(false);
         config.setAutoTaskUniqueID(false);
         config.setAutoOutlineLevel(true);
         config.setAutoOutlineNumber(true);
         config.setAutoWBS(true);
         m_projectFile.getProjectProperties().setFileApplication("GanttProject");
         m_projectFile.getProjectProperties().setFileType("GAN");
         m_eventManager.addProjectListeners(m_projectListeners);
         Project ganttProject = (Project) UnmarshalHelper.unmarshal(CONTEXT, stream);
         readProjectProperties(ganttProject);
         readCalendars(ganttProject);
         readResources(ganttProject);
         readTasks(ganttProject);
         readRelationships(ganttProject);
         readResourceAssignments(ganttProject);
         config.updateUniqueCounters();
         return m_projectFile;
      }
      catch (ParserConfigurationException ex)
      {
         throw new MPXJException("Failed to parse file", ex);
      }
      catch (JAXBException ex)
      {
         throw new MPXJException("Failed to parse file", ex);
      }
      catch (SAXException ex)
      {
         throw new MPXJException("Failed to parse file", ex);
      }
      finally
      {
         m_projectFile = null;
         m_mpxjCalendar = null;
         m_eventManager = null;
         m_projectListeners = null;
         m_localeDateFormat = null;
         m_resourcePropertyDefinitions = null;
         m_taskPropertyDefinitions = null;
         m_roleDefinitions = null;
      }
   }
   private void readProjectProperties(Project ganttProject)
   {
      ProjectProperties mpxjProperties = m_projectFile.getProjectProperties();
      mpxjProperties.setName(ganttProject.getName());
      mpxjProperties.setCompany(ganttProject.getCompany());
      mpxjProperties.setDefaultDurationUnits(TimeUnit.DAYS);
      String locale = ganttProject.getLocale();
      if (locale == null)
      {
         locale = "en_US";
      }
      m_localeDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale(locale));
   }
   private void readCalendars(Project ganttProject)
   {
      m_mpxjCalendar = m_projectFile.addCalendar();
      m_mpxjCalendar.setName(ProjectCalendar.DEFAULT_BASE_CALENDAR_NAME);
      Calendars gpCalendar = ganttProject.getCalendars();
      setWorkingDays(m_mpxjCalendar, gpCalendar);
      setExceptions(m_mpxjCalendar, gpCalendar);
      m_eventManager.fireCalendarReadEvent(m_mpxjCalendar);
   }
   private void setWorkingDays(ProjectCalendar mpxjCalendar, Calendars gpCalendar)
   {
      DayTypes dayTypes = gpCalendar.getDayTypes();
      DefaultWeek defaultWeek = dayTypes.getDefaultWeek();
      if (defaultWeek == null)
      {
         mpxjCalendar.setWorkingDay(Day.SUNDAY, false);
         mpxjCalendar.setWorkingDay(Day.MONDAY, true);
         mpxjCalendar.setWorkingDay(Day.TUESDAY, true);
         mpxjCalendar.setWorkingDay(Day.WEDNESDAY, true);
         mpxjCalendar.setWorkingDay(Day.THURSDAY, true);
         mpxjCalendar.setWorkingDay(Day.FRIDAY, true);
         mpxjCalendar.setWorkingDay(Day.SATURDAY, false);
      }
      else
      {
         mpxjCalendar.setWorkingDay(Day.MONDAY, isWorkingDay(defaultWeek.getMon()));
         mpxjCalendar.setWorkingDay(Day.TUESDAY, isWorkingDay(defaultWeek.getTue()));
         mpxjCalendar.setWorkingDay(Day.WEDNESDAY, isWorkingDay(defaultWeek.getWed()));
         mpxjCalendar.setWorkingDay(Day.THURSDAY, isWorkingDay(defaultWeek.getThu()));
         mpxjCalendar.setWorkingDay(Day.FRIDAY, isWorkingDay(defaultWeek.getFri()));
         mpxjCalendar.setWorkingDay(Day.SATURDAY, isWorkingDay(defaultWeek.getSat()));
         mpxjCalendar.setWorkingDay(Day.SUNDAY, isWorkingDay(defaultWeek.getSun()));
      }
      for (Day day : Day.values())
      {
         if (mpxjCalendar.isWorkingDay(day))
         {
            ProjectCalendarHours hours = mpxjCalendar.addCalendarHours(day);
            hours.addRange(ProjectCalendarWeek.DEFAULT_WORKING_MORNING);
            hours.addRange(ProjectCalendarWeek.DEFAULT_WORKING_AFTERNOON);
         }
      }
   }
   private boolean isWorkingDay(Integer value)
   {
      return NumberHelper.getInt(value) == 0;
   }
   private void setExceptions(ProjectCalendar mpxjCalendar, Calendars gpCalendar)
   {
      List<net.sf.mpxj.ganttproject.schema.Date> dates = gpCalendar.getDate();
      for (net.sf.mpxj.ganttproject.schema.Date date : dates)
      {
         addException(mpxjCalendar, date);
      }
   }
   private void addException(ProjectCalendar mpxjCalendar, net.sf.mpxj.ganttproject.schema.Date date)
   {
      String year = date.getYear();
      if (year == null || year.isEmpty())
      {
      }
      else
      {
         Calendar calendar = DateHelper.popCalendar();
         calendar.set(Calendar.YEAR, Integer.parseInt(year));
         calendar.set(Calendar.MONTH, NumberHelper.getInt(date.getMonth()));
         calendar.set(Calendar.DAY_OF_MONTH, NumberHelper.getInt(date.getDate()));
         Date exceptionDate = calendar.getTime();
         DateHelper.pushCalendar(calendar);
         ProjectCalendarException exception = mpxjCalendar.addCalendarException(exceptionDate, exceptionDate);
         if ("WORKING_DAY".equals(date.getType()))
         {
            exception.addRange(ProjectCalendarWeek.DEFAULT_WORKING_MORNING);
            exception.addRange(ProjectCalendarWeek.DEFAULT_WORKING_AFTERNOON);
         }
      }
   }
   private void readResources(Project ganttProject)
   {
      Resources resources = ganttProject.getResources();
      readResourceCustomPropertyDefinitions(resources);
      readRoleDefinitions(ganttProject);
      for (net.sf.mpxj.ganttproject.schema.Resource gpResource : resources.getResource())
      {
         readResource(gpResource);
      }
   }
   private void readResourceCustomPropertyDefinitions(Resources gpResources)
   {
      CustomField field = m_projectFile.getCustomFields().getCustomField(ResourceField.TEXT1);
      field.setAlias("Phone");
      for (CustomPropertyDefinition definition : gpResources.getCustomPropertyDefinition())
      {
         String type = definition.getType();
         FieldType fieldType = RESOURCE_PROPERTY_TYPES.get(type).getField();
         if (fieldType == null)
         {
            fieldType = RESOURCE_PROPERTY_TYPES.get("text").getField();
         }
         if (fieldType != null)
         {
            field = m_projectFile.getCustomFields().getCustomField(fieldType);
            field.setAlias(definition.getName());
            String defaultValue = definition.getDefaultValue();
            if (defaultValue != null && defaultValue.isEmpty())
            {
               defaultValue = null;
            }
            m_resourcePropertyDefinitions.put(definition.getId(), new Pair<>(fieldType, defaultValue));
         }
      }
   }
   private void readTaskCustomPropertyDefinitions(Tasks gpTasks)
   {
      for (Taskproperty definition : gpTasks.getTaskproperties().getTaskproperty())
      {
         if (!"custom".equals(definition.getType()))
         {
            continue;
         }
         String type = definition.getValuetype();
         FieldType fieldType = TASK_PROPERTY_TYPES.get(type).getField();
         if (fieldType == null)
         {
            fieldType = TASK_PROPERTY_TYPES.get("text").getField();
         }
         if (fieldType != null)
         {
            CustomField field = m_projectFile.getCustomFields().getCustomField(fieldType);
            field.setAlias(definition.getName());
            String defaultValue = definition.getDefaultvalue();
            if (defaultValue != null && defaultValue.isEmpty())
            {
               defaultValue = null;
            }
            m_taskPropertyDefinitions.put(definition.getId(), new Pair<>(fieldType, defaultValue));
         }
      }
   }
   private void readRoleDefinitions(Project gpProject)
   {
      m_roleDefinitions.put("Default:1", "project manager");
      for (Roles roles : gpProject.getRoles())
      {
         if ("Default".equals(roles.getRolesetName()))
         {
            continue;
         }
         for (Role role : roles.getRole())
         {
            m_roleDefinitions.put(role.getId(), role.getName());
         }
      }
   }
   private void readResource(net.sf.mpxj.ganttproject.schema.Resource gpResource)
   {
      Resource mpxjResource = m_projectFile.addResource();
      mpxjResource.setUniqueID(Integer.valueOf(NumberHelper.getInt(gpResource.getId()) + 1));
      mpxjResource.setName(gpResource.getName());
      mpxjResource.setEmailAddress(gpResource.getContacts());
      mpxjResource.setText(1, gpResource.getPhone());
      mpxjResource.setGroup(m_roleDefinitions.get(gpResource.getFunction()));
      net.sf.mpxj.ganttproject.schema.Rate gpRate = gpResource.getRate();
      if (gpRate != null)
      {
         mpxjResource.setStandardRate(new Rate(gpRate.getValueAttribute(), TimeUnit.DAYS));
      }
      readResourceCustomFields(gpResource, mpxjResource);
      m_eventManager.fireResourceReadEvent(mpxjResource);
   }
   private void readResourceCustomFields(net.sf.mpxj.ganttproject.schema.Resource gpResource, Resource mpxjResource)
   {
      Map<FieldType, Object> customFields = new HashMap<>();
      for (Pair<FieldType, String> definition : m_resourcePropertyDefinitions.values())
      {
         customFields.put(definition.getFirst(), definition.getSecond());
      }
      for (CustomResourceProperty property : gpResource.getCustomProperty())
      {
         Pair<FieldType, String> definition = m_resourcePropertyDefinitions.get(property.getDefinitionId());
         if (definition != null)
         {
            String value = property.getValueAttribute();
            if (value.isEmpty())
            {
               value = null;
            }
            if (value != null)
            {
               Object result;
               switch (definition.getFirst().getDataType())
               {
                  case NUMERIC:
                  {
                     if (value.indexOf('.') == -1)
                     {
                        result = Integer.valueOf(value);
                     }
                     else
                     {
                        result = Double.valueOf(value);
                     }
                     break;
                  }
                  case DATE:
                  {
                     try
                     {
                        result = m_localeDateFormat.parse(value);
                     }
                     catch (ParseException ex)
                     {
                        result = null;
                     }
                     break;
                  }
                  case BOOLEAN:
                  {
                     result = Boolean.valueOf(value.equals("true"));
                     break;
                  }
                  default:
                  {
                     result = value;
                     break;
                  }
               }
               if (result != null)
               {
                  customFields.put(definition.getFirst(), result);
               }
            }
         }
      }
      for (Map.Entry<FieldType, Object> item : customFields.entrySet())
      {
         if (item.getValue() != null)
         {
            mpxjResource.set(item.getKey(), item.getValue());
         }
      }
   }
   private void readTaskCustomFields(net.sf.mpxj.ganttproject.schema.Task gpTask, Task mpxjTask)
   {
      Map<FieldType, Object> customFields = new HashMap<>();
      for (Pair<FieldType, String> definition : m_taskPropertyDefinitions.values())
      {
         customFields.put(definition.getFirst(), definition.getSecond());
      }
      for (CustomTaskProperty property : gpTask.getCustomproperty())
      {
         Pair<FieldType, String> definition = m_taskPropertyDefinitions.get(property.getTaskpropertyId());
         if (definition != null)
         {
            String value = property.getValueAttribute();
            if (value.isEmpty())
            {
               value = null;
            }
            if (value != null)
            {
               Object result;
               switch (definition.getFirst().getDataType())
               {
                  case NUMERIC:
                  {
                     if (value.indexOf('.') == -1)
                     {
                        result = Integer.valueOf(value);
                     }
                     else
                     {
                        result = Double.valueOf(value);
                     }
                     break;
                  }
                  case DATE:
                  {
                     try
                     {
                        result = m_dateFormat.parse(value);
                     }
                     catch (ParseException ex)
                     {
                        result = null;
                     }
                     break;
                  }
                  case BOOLEAN:
                  {
                     result = Boolean.valueOf(value.equals("true"));
                     break;
                  }
                  default:
                  {
                     result = value;
                     break;
                  }
               }
               if (result != null)
               {
                  customFields.put(definition.getFirst(), result);
               }
            }
         }
      }
      for (Map.Entry<FieldType, Object> item : customFields.entrySet())
      {
         if (item.getValue() != null)
         {
            mpxjTask.set(item.getKey(), item.getValue());
         }
      }
   }
   private void readTasks(Project gpProject)
   {
      Tasks tasks = gpProject.getTasks();
      readTaskCustomPropertyDefinitions(tasks);
      for (net.sf.mpxj.ganttproject.schema.Task task : tasks.getTask())
      {
         readTask(m_projectFile, task);
      }
   }
   private void readTask(ChildTaskContainer mpxjParent, net.sf.mpxj.ganttproject.schema.Task gpTask)
   {
      Task mpxjTask = mpxjParent.addTask();
      mpxjTask.setUniqueID(Integer.valueOf(NumberHelper.getInt(gpTask.getId()) + 1));
      mpxjTask.setName(gpTask.getName());
      mpxjTask.setPercentageComplete(gpTask.getComplete());
      mpxjTask.setPriority(getPriority(gpTask.getPriority()));
      mpxjTask.setHyperlink(gpTask.getWebLink());
      Duration duration = Duration.getInstance(NumberHelper.getDouble(gpTask.getDuration()), TimeUnit.DAYS);
      mpxjTask.setDuration(duration);
      if (duration.getDuration() == 0)
      {
         mpxjTask.setMilestone(true);
      }
      else
      {
         mpxjTask.setStart(gpTask.getStart());
         mpxjTask.setFinish(m_mpxjCalendar.getDate(gpTask.getStart(), mpxjTask.getDuration(), false));
      }
      mpxjTask.setConstraintDate(gpTask.getThirdDate());
      if (mpxjTask.getConstraintDate() != null)
      {
         mpxjTask.setConstraintType(ConstraintType.START_NO_EARLIER_THAN);
      }
      readTaskCustomFields(gpTask, mpxjTask);
      m_eventManager.fireTaskReadEvent(mpxjTask);
      for (net.sf.mpxj.ganttproject.schema.Task childTask : gpTask.getTask())
      {
         readTask(mpxjTask, childTask);
      }
   }
   private Priority getPriority(Integer gpPriority)
   {
      int result;
      if (gpPriority == null)
      {
         result = Priority.MEDIUM;
      }
      else
      {
         int index = gpPriority.intValue();
         if (index < 0 || index >= PRIORITY.length)
         {
            result = Priority.MEDIUM;
         }
         else
         {
            result = PRIORITY[index];
         }
      }
      return Priority.getInstance(result);
   }
   private void readRelationships(Project gpProject)
   {
      for (net.sf.mpxj.ganttproject.schema.Task gpTask : gpProject.getTasks().getTask())
      {
         readRelationships(gpTask);
      }
   }
   private void readRelationships(net.sf.mpxj.ganttproject.schema.Task gpTask)
   {
      for (Depend depend : gpTask.getDepend())
      {
         Task task1 = m_projectFile.getTaskByUniqueID(Integer.valueOf(NumberHelper.getInt(gpTask.getId()) + 1));
         Task task2 = m_projectFile.getTaskByUniqueID(Integer.valueOf(NumberHelper.getInt(depend.getId()) + 1));
         if (task1 != null && task2 != null)
         {
            Duration lag = Duration.getInstance(NumberHelper.getInt(depend.getDifference()), TimeUnit.DAYS);
            Relation relation = task2.addPredecessor(task1, getRelationType(depend.getType()), lag);
            m_eventManager.fireRelationReadEvent(relation);
         }
      }
   }
   private RelationType getRelationType(Integer gpType)
   {
      RelationType result = null;
      if (gpType != null)
      {
         int index = NumberHelper.getInt(gpType);
         if (index > 0 && index < RELATION.length)
         {
            result = RELATION[index];
         }
      }
      if (result == null)
      {
         result = RelationType.FINISH_START;
      }
      return result;
   }
   private void readResourceAssignments(Project gpProject)
   {
      Allocations allocations = gpProject.getAllocations();
      if (allocations != null)
      {
         for (Allocation allocation : allocations.getAllocation())
         {
            readResourceAssignment(allocation);
         }
      }
   }
   private void readResourceAssignment(Allocation gpAllocation)
   {
      Integer taskID = Integer.valueOf(NumberHelper.getInt(gpAllocation.getTaskId()) + 1);
      Integer resourceID = Integer.valueOf(NumberHelper.getInt(gpAllocation.getResourceId()) + 1);
      Task task = m_projectFile.getTaskByUniqueID(taskID);
      Resource resource = m_projectFile.getResourceByUniqueID(resourceID);
      if (task != null && resource != null)
      {
         ResourceAssignment mpxjAssignment = task.addResourceAssignment(resource);
         mpxjAssignment.setUnits(gpAllocation.getLoad());
         m_eventManager.fireAssignmentReadEvent(mpxjAssignment);
      }
   }
   private ProjectFile m_projectFile;
   private ProjectCalendar m_mpxjCalendar;
   private EventManager m_eventManager;
   private List<ProjectListener> m_projectListeners;
   private DateFormat m_localeDateFormat;
   private DateFormat m_dateFormat;
   private Map<String, Pair<FieldType, String>> m_resourcePropertyDefinitions;
   private Map<String, Pair<FieldType, String>> m_taskPropertyDefinitions;
   private Map<String, String> m_roleDefinitions;
   private static final Map<String, CustomProperty> RESOURCE_PROPERTY_TYPES = new HashMap<>();
   static
   {
      CustomProperty numeric = new CustomProperty(ResourceFieldLists.CUSTOM_NUMBER);
      RESOURCE_PROPERTY_TYPES.put("int", numeric);
      RESOURCE_PROPERTY_TYPES.put("double", numeric);
      RESOURCE_PROPERTY_TYPES.put("text", new CustomProperty(ResourceFieldLists.CUSTOM_TEXT, 1));
      RESOURCE_PROPERTY_TYPES.put("date", new CustomProperty(ResourceFieldLists.CUSTOM_DATE));
      RESOURCE_PROPERTY_TYPES.put("boolean", new CustomProperty(ResourceFieldLists.CUSTOM_FLAG));
   }
   private static final Map<String, CustomProperty> TASK_PROPERTY_TYPES = new HashMap<>();
   static
   {
      CustomProperty numeric = new CustomProperty(TaskFieldLists.CUSTOM_NUMBER);
      TASK_PROPERTY_TYPES.put("int", numeric);
      TASK_PROPERTY_TYPES.put("double", numeric);
      TASK_PROPERTY_TYPES.put("text", new CustomProperty(TaskFieldLists.CUSTOM_TEXT));
      TASK_PROPERTY_TYPES.put("date", new CustomProperty(TaskFieldLists.CUSTOM_DATE));
      TASK_PROPERTY_TYPES.put("boolean", new CustomProperty(TaskFieldLists.CUSTOM_FLAG));
   }
   private static final int[] PRIORITY =
   {
      Priority.LOW, 
      Priority.MEDIUM, 
      Priority.HIGH, 
      Priority.LOWEST, 
      Priority.HIGHEST, 
   };
   static final RelationType[] RELATION =
   {
      null, 
      RelationType.START_START, 
      RelationType.FINISH_START, 
      RelationType.FINISH_FINISH, 
      RelationType.START_FINISH 
   };
   private static JAXBContext CONTEXT;
   private static JAXBException CONTEXT_EXCEPTION;
   static
   {
      try
      {
         System.setProperty("com.sun.xml.bind.v2.runtime.JAXBContextImpl.fastBoot", "true");
         CONTEXT = JAXBContext.newInstance("net.sf.mpxj.ganttproject.schema", GanttProjectReader.class.getClassLoader());
      }
      catch (JAXBException ex)
      {
         CONTEXT_EXCEPTION = ex;
         CONTEXT = null;
      }
   }
}
