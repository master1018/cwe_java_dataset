package com.softwaremill.session.javadsl;
import akka.http.javadsl.model.FormData;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.Cookie;
import akka.http.javadsl.model.headers.HttpCookie;
import akka.http.javadsl.model.headers.RawHeader;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.testkit.TestRouteResult;
import akka.japi.Pair;
import com.softwaremill.session.CsrfCheckMode;
import com.softwaremill.session.SessionContinuity;
import com.softwaremill.session.SetSessionTransport;
import org.junit.Assert;
import org.junit.Test;
public class CsrfDirectivesTest extends HttpSessionAwareDirectivesTest {
    protected Route buildRoute(HttpSessionAwareDirectives<String> testDirectives, SessionContinuity<String> oneOff, SessionContinuity<String> refreshable, SetSessionTransport sessionTransport, CsrfCheckMode<String> csrfCheckMode) {
        return route(
            testDirectives.randomTokenCsrfProtection(csrfCheckMode, () ->
                route(
                    get(() ->
                        path("site", () ->
                            complete("ok")
                        )
                    ),
                    post(() ->
                        route(
                            path("login", () ->
                                testDirectives.setNewCsrfToken(csrfCheckMode, () ->
                                    complete("ok"))),
                            path("transfer_money", () ->
                                complete("ok")
                            )
                        )
                    )
                )
            )
        );
    }
    @Test
    public void shouldSetTheCsrfCookieOnTheFirstGetRequestOnly() {
        final Route route = createCsrfRouteWithCheckHeaderMode();
        TestRouteResult testRouteResult = testRoute(route)
            .run(HttpRequest.GET("/site"));
        testRouteResult
            .assertStatusCode(StatusCodes.OK)
            .assertEntity("ok");
        HttpResponse response = testRouteResult.response();
        HttpCookie csrfCookie = getCsrfTokenCookieValues(response);
        Assert.assertNotNull(csrfCookie.value());
        TestRouteResult testRouteResult2 = testRoute(route)
            .run(HttpRequest.GET("/site")
                .addHeader(Cookie.create(csrfCookieName, csrfCookie.value()))
            );
        testRouteResult2
            .assertStatusCode(StatusCodes.OK)
            .assertEntity("ok");
        HttpResponse response2 = testRouteResult2.response();
        HttpCookie cookieValues2 = getCsrfTokenCookieValues(response2);
        Assert.assertNull(cookieValues2);
    }
    @Test
    public void shouldRejectRequestsIfTheCsrfCookieDoesNotMatchTheHeaderValue() {
        final Route route = createCsrfRouteWithCheckHeaderMode();
        TestRouteResult testRouteResult = testRoute(route)
            .run(HttpRequest.GET("/site"));
        testRouteResult
            .assertStatusCode(StatusCodes.OK);
        HttpCookie csrfCookie = getCsrfTokenCookieValues(testRouteResult.response());
        TestRouteResult testRouteResult2 = testRoute(route)
            .run(HttpRequest.POST("/transfer_money")
                .addHeader(Cookie.create(csrfCookieName, csrfCookie.value()))
                .addHeader(RawHeader.create(csrfSubmittedName, "something else"))
            );
        testRouteResult2
            .assertStatusCode(StatusCodes.FORBIDDEN);
    }
    @Test
    public void shouldRejectRequestsIfTheCsrfCookieIsNotSet() {
        final Route route = createCsrfRouteWithCheckHeaderMode();
        TestRouteResult testRouteResult = testRoute(route)
            .run(HttpRequest.GET("/site"));
        testRouteResult
            .assertStatusCode(StatusCodes.OK);
        TestRouteResult testRouteResult2 = testRoute(route)
            .run(HttpRequest.POST("/transfer_money"));
        testRouteResult2
            .assertStatusCode(StatusCodes.FORBIDDEN);
    }
    @Test
    public void shouldRejectRequestsIfTheCsrfCookieAndTheHeaderAreEmpty() {
        final Route route = createCsrfRouteWithCheckHeaderMode();
        TestRouteResult testRouteResult = testRoute(route)
          .run(HttpRequest.GET("/site"));
        testRouteResult
          .assertStatusCode(StatusCodes.OK);
        TestRouteResult testRouteResult2 = testRoute(route)
          .run(HttpRequest.POST("/transfer_money")
            .addHeader(Cookie.create(csrfCookieName, ""))
            .addHeader(RawHeader.create(csrfSubmittedName, ""))
          );
        testRouteResult2
          .assertStatusCode(StatusCodes.FORBIDDEN);
    }
    @Test
    public void shouldAcceptRequestsIfTheCsrfCookieMatchesTheHeaderValue() {
        final Route route = createCsrfRouteWithCheckHeaderMode();
        TestRouteResult testRouteResult = testRoute(route)
            .run(HttpRequest.GET("/site"));
        testRouteResult
            .assertStatusCode(StatusCodes.OK);
        HttpCookie csrfCookie = getCsrfTokenCookieValues(testRouteResult.response());
        TestRouteResult testRouteResult2 = testRoute(route)
            .run(HttpRequest.POST("/transfer_money")
                .addHeader(Cookie.create(csrfCookieName, csrfCookie.value()))
                .addHeader(RawHeader.create(csrfSubmittedName, csrfCookie.value()))
            );
        testRouteResult2
            .assertStatusCode(StatusCodes.OK)
            .assertEntity("ok");
    }
    @Test
    public void shouldAcceptRequestsIfTheCsrfCookieMatchesTheFormFieldValue() {
        final Route route = createCsrfRouteWithCheckHeaderAndFormMode();
        TestRouteResult testRouteResult = testRoute(route)
            .run(HttpRequest.GET("/site"));
        testRouteResult
            .assertStatusCode(StatusCodes.OK);
        HttpCookie csrfCookie = getCsrfTokenCookieValues(testRouteResult.response());
        final FormData formData = FormData.create(
            Pair.create(csrfSubmittedName, csrfCookie.value())
        );
        TestRouteResult testRouteResult2 = testRoute(route)
            .run(HttpRequest.POST("/transfer_money").withEntity(formData.toEntity())
                .addHeader(Cookie.create(csrfCookieName, csrfCookie.value()))
            );
        testRouteResult2
            .assertStatusCode(StatusCodes.OK)
            .assertEntity("ok");
    }
    @Test
    public void shouldSetANewCsrfCookieWhenRequested() {
        final Route route = createCsrfRouteWithCheckHeaderMode();
        TestRouteResult testRouteResult = testRoute(route)
            .run(HttpRequest.GET("/site"));
        testRouteResult
            .assertStatusCode(StatusCodes.OK);
        HttpCookie csrfCookie = getCsrfTokenCookieValues(testRouteResult.response());
        TestRouteResult testRouteResult2 = testRoute(route)
            .run(HttpRequest.POST("/login")
                .addHeader(Cookie.create(csrfCookieName, csrfCookie.value()))
                .addHeader(RawHeader.create(csrfSubmittedName, csrfCookie.value()))
            );
        testRouteResult2
            .assertStatusCode(StatusCodes.OK)
            .assertEntity("ok");
        HttpCookie csrfCookie2 = getCsrfTokenCookieValues(testRouteResult2.response());
        Assert.assertNotEquals(csrfCookie.value(), csrfCookie2.value());
    }
}
