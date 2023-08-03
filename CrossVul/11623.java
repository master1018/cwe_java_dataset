
package net.sf.mpxj.conceptdraw;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import net.sf.mpxj.DateRange;
import net.sf.mpxj.Duration;
import net.sf.mpxj.EventManager;
import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarException;
import net.sf.mpxj.ProjectCalendarHours;
import net.sf.mpxj.ProjectConfig;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectProperties;
import net.sf.mpxj.Rate;
import net.sf.mpxj.Relation;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.common.AlphanumComparator;
import net.sf.mpxj.common.UnmarshalHelper;
import net.sf.mpxj.conceptdraw.schema.Document;
import net.sf.mpxj.conceptdraw.schema.Document.Calendars.Calendar;
import net.sf.mpxj.conceptdraw.schema.Document.Calendars.Calendar.ExceptedDays.ExceptedDay;
import net.sf.mpxj.conceptdraw.schema.Document.Calendars.Calendar.WeekDays.WeekDay;
import net.sf.mpxj.conceptdraw.schema.Document.Links.Link;
import net.sf.mpxj.conceptdraw.schema.Document.Projects.Project;
import net.sf.mpxj.conceptdraw.schema.Document.WorkspaceProperties;
import net.sf.mpxj.listener.ProjectListener;
import net.sf.mpxj.reader.AbstractProjectReader;
public final class ConceptDrawProjectReader extends AbstractProjectReader
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
         m_calendarMap = new HashMap<>();
         m_taskIdMap = new HashMap<>();
         ProjectConfig config = m_projectFile.getProjectConfig();
         config.setAutoResourceUniqueID(false);
         config.setAutoResourceID(false);
         m_projectFile.getProjectProperties().setFileApplication("ConceptDraw PROJECT");
         m_projectFile.getProjectProperties().setFileType("CDP");
         m_eventManager.addProjectListeners(m_projectListeners);
         Document cdp = (Document) UnmarshalHelper.unmarshal(CONTEXT, stream, new NamespaceFilter());
         readProjectProperties(cdp);
         readCalendars(cdp);
         readResources(cdp);
         readTasks(cdp);
         readRelationships(cdp);
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
      catch (IOException ex)
      {
         throw new MPXJException("Failed to parse file", ex);
      }
      finally
      {
         m_projectFile = null;
         m_eventManager = null;
         m_projectListeners = null;
         m_calendarMap = null;
         m_taskIdMap = null;
      }
   }
   private void readProjectProperties(Document cdp)
   {
      WorkspaceProperties props = cdp.getWorkspaceProperties();
      ProjectProperties mpxjProps = m_projectFile.getProjectProperties();
      mpxjProps.setSymbolPosition(props.getCurrencyPosition());
      mpxjProps.setCurrencyDigits(props.getCurrencyDigits());
      mpxjProps.setCurrencySymbol(props.getCurrencySymbol());
      mpxjProps.setDaysPerMonth(props.getDaysPerMonth());
      mpxjProps.setMinutesPerDay(props.getHoursPerDay());
      mpxjProps.setMinutesPerWeek(props.getHoursPerWeek());
      m_workHoursPerDay = mpxjProps.getMinutesPerDay().doubleValue() / 60.0;
   }
   private void readCalendars(Document cdp)
   {
      for (Calendar calendar : cdp.getCalendars().getCalendar())
      {
         readCalendar(calendar);
      }
      for (Calendar calendar : cdp.getCalendars().getCalendar())
      {
         ProjectCalendar child = m_calendarMap.get(calendar.getID());
         ProjectCalendar parent = m_calendarMap.get(calendar.getBaseCalendarID());
         if (parent == null)
         {
            m_projectFile.setDefaultCalendar(child);
         }
         else
         {
            child.setParent(parent);
         }
      }
   }
   private void readCalendar(Calendar calendar)
   {
      ProjectCalendar mpxjCalendar = m_projectFile.addCalendar();
      mpxjCalendar.setName(calendar.getName());
      m_calendarMap.put(calendar.getID(), mpxjCalendar);
      for (WeekDay day : calendar.getWeekDays().getWeekDay())
      {
         readWeekDay(mpxjCalendar, day);
      }
      for (ExceptedDay day : calendar.getExceptedDays().getExceptedDay())
      {
         readExceptionDay(mpxjCalendar, day);
      }
   }
   private void readWeekDay(ProjectCalendar mpxjCalendar, WeekDay day)
   {
      if (day.isIsDayWorking())
      {
         ProjectCalendarHours hours = mpxjCalendar.addCalendarHours(day.getDay());
         for (Document.Calendars.Calendar.WeekDays.WeekDay.TimePeriods.TimePeriod period : day.getTimePeriods().getTimePeriod())
         {
            hours.addRange(new DateRange(period.getFrom(), period.getTo()));
         }
      }
   }
   private void readExceptionDay(ProjectCalendar mpxjCalendar, ExceptedDay day)
   {
      ProjectCalendarException mpxjException = mpxjCalendar.addCalendarException(day.getDate(), day.getDate());
      if (day.isIsDayWorking())
      {
         for (Document.Calendars.Calendar.ExceptedDays.ExceptedDay.TimePeriods.TimePeriod period : day.getTimePeriods().getTimePeriod())
         {
            mpxjException.addRange(new DateRange(period.getFrom(), period.getTo()));
         }
      }
   }
   private void readResources(Document cdp)
   {
      for (Document.Resources.Resource resource : cdp.getResources().getResource())
      {
         readResource(resource);
      }
   }
   private void readResource(Document.Resources.Resource resource)
   {
      Resource mpxjResource = m_projectFile.addResource();
      mpxjResource.setName(resource.getName());
      mpxjResource.setResourceCalendar(m_calendarMap.get(resource.getCalendarID()));
      mpxjResource.setStandardRate(new Rate(resource.getCost(), resource.getCostTimeUnit()));
      mpxjResource.setEmailAddress(resource.getEMail());
      mpxjResource.setGroup(resource.getGroup());
      mpxjResource.setUniqueID(resource.getID());
      mpxjResource.setNotes(resource.getNote());
      mpxjResource.setID(Integer.valueOf(resource.getOutlineNumber()));
      mpxjResource.setType(resource.getSubType() == null ? resource.getType() : resource.getSubType());
   }
   private void readTasks(Document cdp)
   {
      List<Project> projects = new ArrayList<>(cdp.getProjects().getProject());
      final AlphanumComparator comparator = new AlphanumComparator();
      Collections.sort(projects, new Comparator<Project>()
      {
         @Override public int compare(Project o1, Project o2)
         {
            return comparator.compare(o1.getOutlineNumber(), o2.getOutlineNumber());
         }
      });
      for (Project project : cdp.getProjects().getProject())
      {
         readProject(project);
      }
   }
   private void readProject(Project project)
   {
      Task mpxjTask = m_projectFile.addTask();
      mpxjTask.setBaselineCost(project.getBaselineCost());
      mpxjTask.setBaselineFinish(project.getBaselineFinishDate());
      mpxjTask.setBaselineStart(project.getBaselineStartDate());
      mpxjTask.setFinish(project.getFinishDate());
      mpxjTask.setName(project.getName());
      mpxjTask.setNotes(project.getNote());
      mpxjTask.setPriority(project.getPriority());
      mpxjTask.setStart(project.getStartDate());
      String projectIdentifier = project.getID().toString();
      mpxjTask.setGUID(UUID.nameUUIDFromBytes(projectIdentifier.getBytes()));
      List<Document.Projects.Project.Task> tasks = new ArrayList<>(project.getTask());
      final AlphanumComparator comparator = new AlphanumComparator();
      Collections.sort(tasks, new Comparator<Document.Projects.Project.Task>()
      {
         @Override public int compare(Document.Projects.Project.Task o1, Document.Projects.Project.Task o2)
         {
            return comparator.compare(o1.getOutlineNumber(), o2.getOutlineNumber());
         }
      });
      Map<String, Task> map = new HashMap<>();
      map.put("", mpxjTask);
      for (Document.Projects.Project.Task task : tasks)
      {
         readTask(projectIdentifier, map, task);
      }
   }
   private void readTask(String projectIdentifier, Map<String, Task> map, Document.Projects.Project.Task task)
   {
      Task parentTask = map.get(getParentOutlineNumber(task.getOutlineNumber()));
      Task mpxjTask = parentTask.addTask();
      TimeUnit units = task.getBaseDurationTimeUnit();
      mpxjTask.setCost(task.getActualCost());
      mpxjTask.setDuration(getDuration(units, task.getActualDuration()));
      mpxjTask.setFinish(task.getActualFinishDate());
      mpxjTask.setStart(task.getActualStartDate());
      mpxjTask.setBaselineDuration(getDuration(units, task.getBaseDuration()));
      mpxjTask.setBaselineFinish(task.getBaseFinishDate());
      mpxjTask.setBaselineCost(task.getBaselineCost());
      mpxjTask.setBaselineStart(task.getBaseStartDate());
      mpxjTask.setPercentageComplete(task.getComplete());
      mpxjTask.setDeadline(task.getDeadlineDate());
      mpxjTask.setName(task.getName());
      mpxjTask.setNotes(task.getNote());
      mpxjTask.setPriority(task.getPriority());
      mpxjTask.setType(task.getSchedulingType());
      if (task.isIsMilestone())
      {
         mpxjTask.setMilestone(true);
         mpxjTask.setDuration(Duration.getInstance(0, TimeUnit.HOURS));
         mpxjTask.setBaselineDuration(Duration.getInstance(0, TimeUnit.HOURS));
      }
      String taskIdentifier = projectIdentifier + "." + task.getID();
      m_taskIdMap.put(task.getID(), mpxjTask);
      mpxjTask.setGUID(UUID.nameUUIDFromBytes(taskIdentifier.getBytes()));
      map.put(task.getOutlineNumber(), mpxjTask);
      for (Document.Projects.Project.Task.ResourceAssignments.ResourceAssignment assignment : task.getResourceAssignments().getResourceAssignment())
      {
         readResourceAssignment(mpxjTask, assignment);
      }
   }
   private void readResourceAssignment(Task task, Document.Projects.Project.Task.ResourceAssignments.ResourceAssignment assignment)
   {
      Resource resource = m_projectFile.getResourceByUniqueID(assignment.getResourceID());
      if (resource != null)
      {
         ResourceAssignment mpxjAssignment = task.addResourceAssignment(resource);
         mpxjAssignment.setUniqueID(assignment.getID());
         mpxjAssignment.setWork(Duration.getInstance(assignment.getManHour().doubleValue() * m_workHoursPerDay, TimeUnit.HOURS));
         mpxjAssignment.setUnits(assignment.getUse());
      }
   }
   private void readRelationships(Document cdp)
   {
      for (Link link : cdp.getLinks().getLink())
      {
         readRelationship(link);
      }
   }
   private void readRelationship(Link link)
   {
      Task sourceTask = m_taskIdMap.get(link.getSourceTaskID());
      Task destinationTask = m_taskIdMap.get(link.getDestinationTaskID());
      if (sourceTask != null && destinationTask != null)
      {
         Duration lag = getDuration(link.getLagUnit(), link.getLag());
         RelationType type = link.getType();
         Relation relation = destinationTask.addPredecessor(sourceTask, type, lag);
         relation.setUniqueID(link.getID());
      }
   }
   private Duration getDuration(TimeUnit units, Double duration)
   {
      Duration result = null;
      if (duration != null)
      {
         double durationValue = duration.doubleValue() * 100.0;
         switch (units)
         {
            case MINUTES:
            {
               durationValue *= MINUTES_PER_DAY;
               break;
            }
            case HOURS:
            {
               durationValue *= HOURS_PER_DAY;
               break;
            }
            case DAYS:
            {
               durationValue *= 3.0;
               break;
            }
            case WEEKS:
            {
               durationValue *= 0.6;
               break;
            }
            case MONTHS:
            {
               durationValue *= 0.15;
               break;
            }
            default:
            {
               throw new IllegalArgumentException("Unsupported time units " + units);
            }
         }
         durationValue = Math.round(durationValue) / 100.0;
         result = Duration.getInstance(durationValue, units);
      }
      return result;
   }
   private String getParentOutlineNumber(String outlineNumber)
   {
      String result;
      int index = outlineNumber.lastIndexOf('.');
      if (index == -1)
      {
         result = "";
      }
      else
      {
         result = outlineNumber.substring(0, index);
      }
      return result;
   }
   private ProjectFile m_projectFile;
   private EventManager m_eventManager;
   private List<ProjectListener> m_projectListeners;
   private Map<Integer, ProjectCalendar> m_calendarMap;
   private Map<Integer, Task> m_taskIdMap;
   private double m_workHoursPerDay;
   private static final int HOURS_PER_DAY = 24;
   private static final int MINUTES_PER_DAY = HOURS_PER_DAY * 60;
   private static JAXBContext CONTEXT;
   private static JAXBException CONTEXT_EXCEPTION;
   static
   {
      try
      {
         System.setProperty("com.sun.xml.bind.v2.runtime.JAXBContextImpl.fastBoot", "true");
         CONTEXT = JAXBContext.newInstance("net.sf.mpxj.conceptdraw.schema", ConceptDrawProjectReader.class.getClassLoader());
      }
      catch (JAXBException ex)
      {
         CONTEXT_EXCEPTION = ex;
         CONTEXT = null;
      }
   }
}
