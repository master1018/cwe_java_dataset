package org.jboss.seam.remoting;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.seam.contexts.RemotingLifecycle;
import org.jboss.seam.core.ConversationPropagation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;
import org.jboss.seam.remoting.wrapper.Wrapper;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.util.XML;
public class ExecutionHandler extends BaseRequestHandler implements RequestHandler
{
   private static final LogProvider log = Logging.getLogProvider(ExecutionHandler.class);
  private static final byte[] HEADER_OPEN = "<header>".getBytes();
  private static final byte[] HEADER_CLOSE = "</header>".getBytes();
  private static final byte[] CONVERSATION_ID_TAG_OPEN = "<conversationId>".getBytes();
  private static final byte[] CONVERSATION_ID_TAG_CLOSE = "</conversationId>".getBytes();
  private static final byte[] CONTEXT_TAG_OPEN = "<context>".getBytes();
  private static final byte[] CONTEXT_TAG_CLOSE = "</context>".getBytes();
  public void handle(HttpServletRequest request, final HttpServletResponse response)
      throws Exception
  {
      response.setContentType("text/xml");
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte[] buffer = new byte[256];
      int read = request.getInputStream().read(buffer);
      while (read != -1)
      {
         out.write(buffer, 0, read);
         read = request.getInputStream().read(buffer);
      }
      String requestData = new String(out.toByteArray());
      log.debug("Processing remote request: " + requestData);
      SAXReader xmlReader = XML.getSafeSaxReader();
      Document doc = xmlReader.read( new StringReader(requestData) );
      final Element env = doc.getRootElement();
      final RequestContext ctx = unmarshalContext(env);
      RemotingLifecycle.restorePageContext();
      new ContextualHttpServletRequest(request)
      {
         @Override
         public void process() throws Exception
         {
            List<Call> calls = unmarshalCalls(env);
            for (Call call : calls) 
            {
               call.execute();
            }               
            ctx.setConversationId( Manager.instance().getCurrentConversationId() );               
            marshalResponse(calls, ctx, response.getOutputStream());               
         }
         @Override
         protected void restoreConversationId()
         {
            ConversationPropagation.instance().setConversationId( ctx.getConversationId() );
         }
         @Override
         protected void handleConversationPropagation() {}
      }.run();
  }
  private RequestContext unmarshalContext(Element env)
  {
    RequestContext ctx = new RequestContext();
    Element header = env.element("header");
    if (header != null)
    {
      Element context = header.element("context");
      if (context != null)
      {
        Element convId = context.element("conversationId");
        if (convId != null)
        {
          ctx.setConversationId(convId.getText());
        }
      }
    }
    return ctx;
  }
  private List<Call> unmarshalCalls(Element env) throws Exception
  {
    try 
    {
      List<Call> calls = new ArrayList<Call>();
      List<Element> callElements = env.element("body").elements("call");
      for (Element e : callElements) 
      {
        Call call = new Call(e.attributeValue("id"),
                             e.attributeValue("component"),
                             e.attributeValue("method"));
        Element refsNode = e.element("refs");
        Iterator iter = refsNode.elementIterator("ref");
        while (iter.hasNext())
        {
          call.getContext().createWrapperFromElement((Element) iter.next());
        }
        for (Wrapper w : call.getContext().getInRefs().values())
        {
          w.unmarshal();
        }
        Element paramsNode = e.element("params");
        iter = paramsNode.elementIterator("param");
        while (iter.hasNext()) 
        {
          Element param = (Element) iter.next();
          call.addParameter(call.getContext().createWrapperFromElement(
            (Element) param.elementIterator().next()));
        }
        calls.add(call);
      }
      return calls;
    }
    catch (Exception ex) 
    {
      log.error("Error unmarshalling calls from request", ex);
      throw ex;
    }
  }
  private void marshalResponse(List<Call> calls, RequestContext ctx, OutputStream out)
      throws IOException
  {
    out.write(ENVELOPE_TAG_OPEN);
    if (ctx.getConversationId() != null)
    {
      out.write(HEADER_OPEN);
      out.write(CONTEXT_TAG_OPEN);
      out.write(CONVERSATION_ID_TAG_OPEN);
      out.write(ctx.getConversationId().getBytes());
      out.write(CONVERSATION_ID_TAG_CLOSE);
      out.write(CONTEXT_TAG_CLOSE);
      out.write(HEADER_CLOSE);
    }
    out.write(BODY_TAG_OPEN);
    for (Call call : calls)
    {
      MarshalUtils.marshalResult(call, out);
    }
    out.write(BODY_TAG_CLOSE);
    out.write(ENVELOPE_TAG_CLOSE);
    out.flush();
  }
}
