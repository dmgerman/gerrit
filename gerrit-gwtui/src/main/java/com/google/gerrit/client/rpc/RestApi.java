begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestBuilder
operator|.
name|DELETE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestBuilder
operator|.
name|GET
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestBuilder
operator|.
name|POST
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestBuilder
operator|.
name|PUT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|RpcStatus
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestBuilder
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|RequestException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|http
operator|.
name|client
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|json
operator|.
name|client
operator|.
name|JSONException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|json
operator|.
name|client
operator|.
name|JSONObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|json
operator|.
name|client
operator|.
name|JSONParser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|json
operator|.
name|client
operator|.
name|JSONValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|StatusCodeException
import|;
end_import

begin_comment
comment|/** Makes a REST API call to the server. */
end_comment

begin_class
DECL|class|RestApi
specifier|public
class|class
name|RestApi
block|{
DECL|field|SC_UNAVAILABLE
specifier|private
specifier|static
specifier|final
name|int
name|SC_UNAVAILABLE
init|=
literal|2
decl_stmt|;
DECL|field|SC_BAD_TRANSPORT
specifier|private
specifier|static
specifier|final
name|int
name|SC_BAD_TRANSPORT
init|=
literal|3
decl_stmt|;
DECL|field|SC_BAD_RESPONSE
specifier|private
specifier|static
specifier|final
name|int
name|SC_BAD_RESPONSE
init|=
literal|4
decl_stmt|;
DECL|field|JSON_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|JSON_TYPE
init|=
literal|"application/json"
decl_stmt|;
DECL|field|TEXT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|TEXT_TYPE
init|=
literal|"text/plain"
decl_stmt|;
comment|/**    * Expected JSON content body prefix that prevents XSSI.    *<p>    * The server always includes this line as the first line of the response    * content body when the response body is formatted as JSON. It gets inserted    * by the server to prevent the resource from being imported into another    * domain's page using a&lt;script&gt; tag. This line must be removed before    * the JSON can be parsed.    */
DECL|field|JSON_MAGIC
specifier|private
specifier|static
specifier|final
name|String
name|JSON_MAGIC
init|=
literal|")]}'\n"
decl_stmt|;
comment|/** True if err is a StatusCodeException reporting Not Found. */
DECL|method|isNotFound (Throwable err)
specifier|public
specifier|static
name|boolean
name|isNotFound
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
return|return
name|isStatus
argument_list|(
name|err
argument_list|,
name|Response
operator|.
name|SC_NOT_FOUND
argument_list|)
return|;
block|}
comment|/** True if err is describing a user that is currently anonymous. */
DECL|method|isNotSignedIn (Throwable err)
specifier|public
specifier|static
name|boolean
name|isNotSignedIn
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
if|if
condition|(
name|err
operator|instanceof
name|StatusCodeException
condition|)
block|{
name|StatusCodeException
name|sce
init|=
operator|(
name|StatusCodeException
operator|)
name|err
decl_stmt|;
if|if
condition|(
name|sce
operator|.
name|getStatusCode
argument_list|()
operator|==
name|Response
operator|.
name|SC_UNAUTHORIZED
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|sce
operator|.
name|getStatusCode
argument_list|()
operator|==
name|Response
operator|.
name|SC_FORBIDDEN
operator|&&
operator|(
name|sce
operator|.
name|getEncodedResponse
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Authentication required"
argument_list|)
operator|||
name|sce
operator|.
name|getEncodedResponse
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Must be signed-in"
argument_list|)
operator|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** True if err is a StatusCodeException with a specific HTTP code. */
DECL|method|isStatus (Throwable err, int status)
specifier|public
specifier|static
name|boolean
name|isStatus
parameter_list|(
name|Throwable
name|err
parameter_list|,
name|int
name|status
parameter_list|)
block|{
return|return
name|err
operator|instanceof
name|StatusCodeException
operator|&&
operator|(
operator|(
name|StatusCodeException
operator|)
name|err
operator|)
operator|.
name|getStatusCode
argument_list|()
operator|==
name|status
return|;
block|}
comment|/** Is the Gerrit Code Review server likely to return this status? */
DECL|method|isExpected (int statusCode)
specifier|public
specifier|static
name|boolean
name|isExpected
parameter_list|(
name|int
name|statusCode
parameter_list|)
block|{
switch|switch
condition|(
name|statusCode
condition|)
block|{
case|case
name|SC_UNAVAILABLE
case|:
case|case
literal|400
case|:
comment|// Bad Request
case|case
literal|401
case|:
comment|// Unauthorized
case|case
literal|403
case|:
comment|// Forbidden
case|case
literal|404
case|:
comment|// Not Found
case|case
literal|405
case|:
comment|// Method Not Allowed
case|case
literal|409
case|:
comment|// Conflict
case|case
literal|412
case|:
comment|// Precondition Failed
case|case
literal|429
case|:
comment|// Too Many Requests (RFC 6585)
return|return
literal|true
return|;
default|default:
comment|// Assume any other code is not expected. These may be
comment|// local proxy server errors outside of our control.
return|return
literal|false
return|;
block|}
block|}
DECL|class|MyRequestCallback
specifier|private
specifier|static
class|class
name|MyRequestCallback
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
implements|implements
name|RequestCallback
block|{
DECL|field|cb
specifier|private
specifier|final
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
decl_stmt|;
DECL|method|MyRequestCallback (AsyncCallback<T> cb)
name|MyRequestCallback
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|this
operator|.
name|cb
operator|=
name|cb
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onResponseReceived (Request req, Response res)
specifier|public
name|void
name|onResponseReceived
parameter_list|(
name|Request
name|req
parameter_list|,
name|Response
name|res
parameter_list|)
block|{
name|int
name|status
init|=
name|res
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|Response
operator|.
name|SC_NO_CONTENT
condition|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcComplete
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|200
operator|<=
name|status
operator|&&
name|status
operator|<
literal|300
condition|)
block|{
if|if
condition|(
operator|!
name|isJsonBody
argument_list|(
name|res
argument_list|)
condition|)
block|{
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcComplete
argument_list|()
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
operator|new
name|StatusCodeException
argument_list|(
name|SC_BAD_RESPONSE
argument_list|,
literal|"Expected "
operator|+
name|JSON_TYPE
operator|+
literal|"; received Content-Type: "
operator|+
name|res
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|T
name|data
decl_stmt|;
try|try
block|{
comment|// javac generics bug
name|data
operator|=
name|RestApi
operator|.
expr|<
name|T
operator|>
name|cast
argument_list|(
name|parseJson
argument_list|(
name|res
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSONException
name|e
parameter_list|)
block|{
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcComplete
argument_list|()
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
operator|new
name|StatusCodeException
argument_list|(
name|SC_BAD_RESPONSE
argument_list|,
literal|"Invalid JSON: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|cb
operator|.
name|onSuccess
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcComplete
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|msg
decl_stmt|;
if|if
condition|(
name|isTextBody
argument_list|(
name|res
argument_list|)
condition|)
block|{
name|msg
operator|=
name|res
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isJsonBody
argument_list|(
name|res
argument_list|)
condition|)
block|{
name|JSONValue
name|v
decl_stmt|;
try|try
block|{
name|v
operator|=
name|parseJson
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSONException
name|e
parameter_list|)
block|{
name|v
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|v
operator|!=
literal|null
operator|&&
name|v
operator|.
name|isString
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|v
operator|.
name|isString
argument_list|()
operator|.
name|stringValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|trimJsonMagic
argument_list|(
name|res
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|msg
operator|=
name|res
operator|.
name|getStatusText
argument_list|()
expr_stmt|;
block|}
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcComplete
argument_list|()
expr_stmt|;
name|cb
operator|.
name|onFailure
argument_list|(
operator|new
name|StatusCodeException
argument_list|(
name|status
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onError (Request req, Throwable err)
specifier|public
name|void
name|onError
parameter_list|(
name|Request
name|req
parameter_list|,
name|Throwable
name|err
parameter_list|)
block|{
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcComplete
argument_list|()
expr_stmt|;
if|if
condition|(
name|err
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"XmlHttpRequest.status"
argument_list|)
condition|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
operator|new
name|StatusCodeException
argument_list|(
name|SC_UNAVAILABLE
argument_list|,
name|RpcConstants
operator|.
name|C
operator|.
name|errorServerUnavailable
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cb
operator|.
name|onFailure
argument_list|(
operator|new
name|StatusCodeException
argument_list|(
name|SC_BAD_TRANSPORT
argument_list|,
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|field|url
specifier|private
name|StringBuilder
name|url
decl_stmt|;
DECL|field|hasQueryParams
specifier|private
name|boolean
name|hasQueryParams
decl_stmt|;
DECL|field|contentType
specifier|private
name|String
name|contentType
decl_stmt|;
DECL|field|contentData
specifier|private
name|String
name|contentData
decl_stmt|;
DECL|field|ifNoneMatch
specifier|private
name|String
name|ifNoneMatch
decl_stmt|;
comment|/**    * Initialize a new API call.    *<p>    * By default the JSON format will be selected by including an HTTP Accept    * header in the request.    *    * @param name URL of the REST resource to access, e.g. {@code "/projects/"}    *        to list accessible projects from the server.    */
DECL|method|RestApi (String name)
specifier|public
name|RestApi
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|url
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|GWT
operator|.
name|getHostPageBaseURL
argument_list|()
argument_list|)
expr_stmt|;
name|url
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|view (String name)
specifier|public
name|RestApi
name|view
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|idRaw
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|id (String id)
specifier|public
name|RestApi
name|id
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|idRaw
argument_list|(
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|idRaw (String name)
specifier|public
name|RestApi
name|idRaw
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|hasQueryParams
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
if|if
condition|(
name|url
operator|.
name|charAt
argument_list|(
name|url
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|!=
literal|'/'
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
name|url
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addParameter (String name, String value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, String... value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|String
modifier|...
name|value
parameter_list|)
block|{
for|for
control|(
name|String
name|val
range|:
name|value
control|)
block|{
name|addParameter
argument_list|(
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|addParameterTrue (String name)
specifier|public
name|RestApi
name|addParameterTrue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, boolean value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|value
condition|?
literal|"t"
else|:
literal|"f"
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, int value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addParameter (String name, Enum<?> value)
specifier|public
name|RestApi
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Enum
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|addParameterRaw
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addParameterRaw (String name, String value)
specifier|public
name|RestApi
name|addParameterRaw
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|hasQueryParams
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|url
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|hasQueryParams
operator|=
literal|true
expr_stmt|;
block|}
name|url
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|ifNoneMatch ()
specifier|public
name|RestApi
name|ifNoneMatch
parameter_list|()
block|{
return|return
name|ifNoneMatch
argument_list|(
literal|"*"
argument_list|)
return|;
block|}
DECL|method|ifNoneMatch (String etag)
specifier|public
name|RestApi
name|ifNoneMatch
parameter_list|(
name|String
name|etag
parameter_list|)
block|{
name|ifNoneMatch
operator|=
name|etag
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|data (JavaScriptObject obj)
specifier|public
name|RestApi
name|data
parameter_list|(
name|JavaScriptObject
name|obj
parameter_list|)
block|{
return|return
name|data
argument_list|(
operator|new
name|JSONObject
argument_list|(
name|obj
argument_list|)
argument_list|)
return|;
block|}
DECL|method|data (JSONObject obj)
specifier|public
name|RestApi
name|data
parameter_list|(
name|JSONObject
name|obj
parameter_list|)
block|{
name|contentType
operator|=
name|JSON_TYPE
operator|+
literal|"; charset=utf-8"
expr_stmt|;
name|contentData
operator|=
name|obj
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|data (String data)
specifier|public
name|RestApi
name|data
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|contentType
operator|=
name|TEXT_TYPE
operator|+
literal|"; charset=utf-8"
expr_stmt|;
name|contentData
operator|=
name|data
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|get (AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|get
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|send
argument_list|(
name|GET
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|put (AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|put
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|send
argument_list|(
name|PUT
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|delete
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|send
argument_list|(
name|DELETE
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|post (AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|post
parameter_list|(
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|send
argument_list|(
name|POST
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|url ()
specifier|public
name|String
name|url
parameter_list|()
block|{
return|return
name|url
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|send ( Method method, final AsyncCallback<T> cb)
specifier|public
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|void
name|send
parameter_list|(
name|Method
name|method
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|cb
parameter_list|)
block|{
name|RequestBuilder
name|req
init|=
operator|new
name|RequestBuilder
argument_list|(
name|method
argument_list|,
name|url
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ifNoneMatch
operator|!=
literal|null
condition|)
block|{
name|req
operator|.
name|setHeader
argument_list|(
literal|"If-None-Match"
argument_list|,
name|ifNoneMatch
argument_list|)
expr_stmt|;
block|}
name|req
operator|.
name|setHeader
argument_list|(
literal|"Accept"
argument_list|,
name|JSON_TYPE
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getAuthorization
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|req
operator|.
name|setHeader
argument_list|(
literal|"Authorization"
argument_list|,
name|Gerrit
operator|.
name|getAuthorization
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentData
operator|!=
literal|null
condition|)
block|{
name|req
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
block|}
name|MyRequestCallback
argument_list|<
name|T
argument_list|>
name|httpCallback
init|=
operator|new
name|MyRequestCallback
argument_list|<
name|T
argument_list|>
argument_list|(
name|cb
argument_list|)
decl_stmt|;
try|try
block|{
name|RpcStatus
operator|.
name|INSTANCE
operator|.
name|onRpcStart
argument_list|()
expr_stmt|;
name|req
operator|.
name|sendRequest
argument_list|(
name|contentData
argument_list|,
name|httpCallback
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RequestException
name|e
parameter_list|)
block|{
name|httpCallback
operator|.
name|onError
argument_list|(
literal|null
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isJsonBody (Response res)
specifier|private
specifier|static
name|boolean
name|isJsonBody
parameter_list|(
name|Response
name|res
parameter_list|)
block|{
return|return
name|isContentType
argument_list|(
name|res
argument_list|,
name|JSON_TYPE
argument_list|)
return|;
block|}
DECL|method|isTextBody (Response res)
specifier|private
specifier|static
name|boolean
name|isTextBody
parameter_list|(
name|Response
name|res
parameter_list|)
block|{
return|return
name|isContentType
argument_list|(
name|res
argument_list|,
name|TEXT_TYPE
argument_list|)
return|;
block|}
DECL|method|isContentType (Response res, String want)
specifier|private
specifier|static
name|boolean
name|isContentType
parameter_list|(
name|Response
name|res
parameter_list|,
name|String
name|want
parameter_list|)
block|{
name|String
name|type
init|=
name|res
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|semi
init|=
name|type
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
if|if
condition|(
name|semi
operator|>=
literal|0
condition|)
block|{
name|type
operator|=
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|semi
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|want
operator|.
name|equals
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|parseJson (Response res)
specifier|private
specifier|static
name|JSONValue
name|parseJson
parameter_list|(
name|Response
name|res
parameter_list|)
throws|throws
name|JSONException
block|{
name|String
name|json
init|=
name|trimJsonMagic
argument_list|(
name|res
operator|.
name|getText
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|JSONException
argument_list|(
literal|"response was empty"
argument_list|)
throw|;
block|}
return|return
name|JSONParser
operator|.
name|parseStrict
argument_list|(
name|json
argument_list|)
return|;
block|}
DECL|method|trimJsonMagic (String json)
specifier|private
specifier|static
name|String
name|trimJsonMagic
parameter_list|(
name|String
name|json
parameter_list|)
block|{
if|if
condition|(
name|json
operator|.
name|startsWith
argument_list|(
name|JSON_MAGIC
argument_list|)
condition|)
block|{
name|json
operator|=
name|json
operator|.
name|substring
argument_list|(
name|JSON_MAGIC
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|json
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|cast (JSONValue val)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
name|T
name|cast
parameter_list|(
name|JSONValue
name|val
parameter_list|)
block|{
if|if
condition|(
name|val
operator|.
name|isObject
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|val
operator|.
name|isObject
argument_list|()
operator|.
name|getJavaScriptObject
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|.
name|isArray
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|val
operator|.
name|isArray
argument_list|()
operator|.
name|getJavaScriptObject
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|.
name|isString
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|NativeString
operator|.
name|wrap
argument_list|(
name|val
operator|.
name|isString
argument_list|()
operator|.
name|stringValue
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|val
operator|.
name|isNull
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|JSONException
argument_list|(
literal|"unsupported JSON type"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

