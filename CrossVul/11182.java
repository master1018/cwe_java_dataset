package org.jboss.seam.remoting;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.core.Manager;
import org.jboss.seam.remoting.messaging.RemoteSubscriber;
import org.jboss.seam.remoting.messaging.SubscriptionRegistry;
import org.jboss.seam.remoting.messaging.SubscriptionRequest;
import org.jboss.seam.util.XML;
import org.jboss.seam.web.ServletContexts;
public class SubscriptionHandler extends BaseRequestHandler implements RequestHandler
{
  public void handle(HttpServletRequest request, HttpServletResponse response)
      throws Exception
  {
    response.setContentType("text/xml");
    SAXReader xmlReader = XML.getSafeSaxReader();
    Document doc = xmlReader.read(request.getInputStream());
    Element env = doc.getRootElement();
    Element body = env.element("body");
    List<SubscriptionRequest> requests = new ArrayList<SubscriptionRequest>();
    List<Element> elements = body.elements("subscribe");
    for (Element e : elements)
    {
      requests.add(new SubscriptionRequest(e.attributeValue("topic")));
    }
    ServletLifecycle.beginRequest(request);
    try
    {
      ServletContexts.instance().setRequest(request);
      Manager.instance().initializeTemporaryConversation();
      ServletLifecycle.resumeConversation(request);
      for (SubscriptionRequest req : requests)
      {
        req.subscribe();
      }
      List<String> unsubscribeTokens = new ArrayList<String>();
      elements = body.elements("unsubscribe");
      for (Element e : elements) 
      {
        unsubscribeTokens.add(e.attributeValue("token"));
      }
      for (String token : unsubscribeTokens) 
      {
        RemoteSubscriber subscriber = SubscriptionRegistry.instance().
                                      getSubscription(token);
        if (subscriber != null)
        {
          subscriber.unsubscribe();
        }
      }
    }
    finally
    {
      Lifecycle.endRequest();
    }
    marshalResponse(requests, response.getOutputStream());
  }
  private void marshalResponse(List<SubscriptionRequest> requests, OutputStream out)
      throws IOException
  {
    out.write(ENVELOPE_TAG_OPEN);
    out.write(BODY_TAG_OPEN);
    for (SubscriptionRequest req : requests)
    {
      req.marshal(out);
    }
    out.write(BODY_TAG_CLOSE);
    out.write(ENVELOPE_TAG_CLOSE);
    out.flush();
  }
}
