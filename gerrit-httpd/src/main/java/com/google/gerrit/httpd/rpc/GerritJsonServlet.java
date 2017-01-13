begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MultimapBuilder
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
name|audit
operator|.
name|AuditService
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
name|audit
operator|.
name|RpcAuditEvent
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
name|common
operator|.
name|TimeUtil
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
name|common
operator|.
name|audit
operator|.
name|Audit
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
name|common
operator|.
name|auth
operator|.
name|SignInRequired
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
name|common
operator|.
name|errors
operator|.
name|NotSignedInException
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicItem
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
name|httpd
operator|.
name|WebSession
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
name|server
operator|.
name|AccessPath
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
name|server
operator|.
name|CurrentUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|GsonBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|RemoteJsonService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|ActiveCall
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|JsonServlet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|MethodHandle
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_comment
comment|/**  * Base JSON servlet to ensure the current user is not forged.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|GerritJsonServlet
specifier|final
class|class
name|GerritJsonServlet
extends|extends
name|JsonServlet
argument_list|<
name|GerritJsonServlet
operator|.
name|GerritCall
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GerritJsonServlet
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|currentCall
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|GerritCall
argument_list|>
name|currentCall
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|currentMethod
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|MethodHandle
argument_list|>
name|currentMethod
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
DECL|field|service
specifier|private
specifier|final
name|RemoteJsonService
name|service
decl_stmt|;
DECL|field|audit
specifier|private
specifier|final
name|AuditService
name|audit
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritJsonServlet (final DynamicItem<WebSession> w, final RemoteJsonService s, final AuditService a)
name|GerritJsonServlet
parameter_list|(
specifier|final
name|DynamicItem
argument_list|<
name|WebSession
argument_list|>
name|w
parameter_list|,
specifier|final
name|RemoteJsonService
name|s
parameter_list|,
specifier|final
name|AuditService
name|a
parameter_list|)
block|{
name|session
operator|=
name|w
expr_stmt|;
name|service
operator|=
name|s
expr_stmt|;
name|audit
operator|=
name|a
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createActiveCall (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|protected
name|GerritCall
name|createActiveCall
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
block|{
specifier|final
name|GerritCall
name|call
init|=
operator|new
name|GerritCall
argument_list|(
name|session
operator|.
name|get
argument_list|()
argument_list|,
name|req
argument_list|,
operator|new
name|AuditedHttpServletResponse
argument_list|(
name|rsp
argument_list|)
argument_list|)
decl_stmt|;
name|currentCall
operator|.
name|set
argument_list|(
name|call
argument_list|)
expr_stmt|;
return|return
name|call
return|;
block|}
annotation|@
name|Override
DECL|method|createGsonBuilder ()
specifier|protected
name|GsonBuilder
name|createGsonBuilder
parameter_list|()
block|{
return|return
name|gerritDefaultGsonBuilder
argument_list|()
return|;
block|}
DECL|method|gerritDefaultGsonBuilder ()
specifier|private
specifier|static
name|GsonBuilder
name|gerritDefaultGsonBuilder
parameter_list|()
block|{
specifier|final
name|GsonBuilder
name|g
init|=
name|defaultGsonBuilder
argument_list|()
decl_stmt|;
name|g
operator|.
name|registerTypeAdapter
argument_list|(
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
operator|.
name|class
argument_list|,
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|EditDeserializer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|g
return|;
block|}
annotation|@
name|Override
DECL|method|preInvoke (final GerritCall call)
specifier|protected
name|void
name|preInvoke
parameter_list|(
specifier|final
name|GerritCall
name|call
parameter_list|)
block|{
name|super
operator|.
name|preInvoke
argument_list|(
name|call
argument_list|)
expr_stmt|;
if|if
condition|(
name|call
operator|.
name|isComplete
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|call
operator|.
name|getMethod
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|SignInRequired
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// If SignInRequired is set on this method we must have both a
comment|// valid XSRF token *and* have the user signed in. Doing these
comment|// checks also validates that they agree on the user identity.
comment|//
if|if
condition|(
operator|!
name|call
operator|.
name|requireXsrfValid
argument_list|()
operator|||
operator|!
name|session
operator|.
name|get
argument_list|()
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|call
operator|.
name|onFailure
argument_list|(
operator|new
name|NotSignedInException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createServiceHandle ()
specifier|protected
name|Object
name|createServiceHandle
parameter_list|()
block|{
return|return
name|service
return|;
block|}
annotation|@
name|Override
DECL|method|service (final HttpServletRequest req, final HttpServletResponse resp)
specifier|protected
name|void
name|service
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|super
operator|.
name|service
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|audit
argument_list|()
expr_stmt|;
name|currentCall
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|audit ()
specifier|private
name|void
name|audit
parameter_list|()
block|{
try|try
block|{
name|GerritCall
name|call
init|=
name|currentCall
operator|.
name|get
argument_list|()
decl_stmt|;
name|MethodHandle
name|method
init|=
name|call
operator|.
name|getMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Audit
name|note
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Audit
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|note
operator|!=
literal|null
condition|)
block|{
name|String
name|sid
init|=
name|call
operator|.
name|getWebSession
argument_list|()
operator|.
name|getSessionId
argument_list|()
decl_stmt|;
name|CurrentUser
name|username
init|=
name|call
operator|.
name|getWebSession
argument_list|()
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|args
init|=
name|extractParams
argument_list|(
name|note
argument_list|,
name|call
argument_list|)
decl_stmt|;
name|String
name|what
init|=
name|extractWhat
argument_list|(
name|note
argument_list|,
name|call
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|call
operator|.
name|getResult
argument_list|()
decl_stmt|;
name|audit
operator|.
name|dispatch
argument_list|(
operator|new
name|RpcAuditEvent
argument_list|(
name|sid
argument_list|,
name|username
argument_list|,
name|what
argument_list|,
name|call
operator|.
name|getWhen
argument_list|()
argument_list|,
name|args
argument_list|,
name|call
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|,
name|call
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|,
operator|(
call|(
name|AuditedHttpServletResponse
call|)
argument_list|(
name|call
operator|.
name|getHttpServletResponse
argument_list|()
argument_list|)
operator|)
operator|.
name|getStatus
argument_list|()
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|all
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to log the call"
argument_list|,
name|all
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|extractParams (Audit note, GerritCall call)
specifier|private
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|extractParams
parameter_list|(
name|Audit
name|note
parameter_list|,
name|GerritCall
name|call
parameter_list|)
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Object
index|[]
name|params
init|=
name|call
operator|.
name|getParams
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|params
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|args
operator|.
name|put
argument_list|(
literal|"$"
operator|+
name|i
argument_list|,
name|params
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|idx
range|:
name|note
operator|.
name|obfuscate
argument_list|()
control|)
block|{
name|args
operator|.
name|removeAll
argument_list|(
literal|"$"
operator|+
name|idx
argument_list|)
expr_stmt|;
name|args
operator|.
name|put
argument_list|(
literal|"$"
operator|+
name|idx
argument_list|,
literal|"*****"
argument_list|)
expr_stmt|;
block|}
return|return
name|args
return|;
block|}
DECL|method|extractWhat (final Audit note, final GerritCall call)
specifier|private
name|String
name|extractWhat
parameter_list|(
specifier|final
name|Audit
name|note
parameter_list|,
specifier|final
name|GerritCall
name|call
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|methodClass
init|=
name|call
operator|.
name|getMethodClass
argument_list|()
decl_stmt|;
name|String
name|methodClassName
init|=
name|methodClass
operator|!=
literal|null
condition|?
name|methodClass
operator|.
name|getName
argument_list|()
else|:
literal|"<UNKNOWN_CLASS>"
decl_stmt|;
name|methodClassName
operator|=
name|methodClassName
operator|.
name|substring
argument_list|(
name|methodClassName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|what
init|=
name|note
operator|.
name|action
argument_list|()
decl_stmt|;
if|if
condition|(
name|what
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|what
operator|=
name|call
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|methodClassName
operator|+
literal|"."
operator|+
name|what
return|;
block|}
DECL|class|GerritCall
specifier|static
class|class
name|GerritCall
extends|extends
name|ActiveCall
block|{
DECL|field|session
specifier|private
specifier|final
name|WebSession
name|session
decl_stmt|;
DECL|field|when
specifier|private
specifier|final
name|long
name|when
decl_stmt|;
DECL|field|resultField
specifier|private
specifier|static
specifier|final
name|Field
name|resultField
decl_stmt|;
DECL|field|methodField
specifier|private
specifier|static
specifier|final
name|Field
name|methodField
decl_stmt|;
comment|// Needed to allow access to non-public result field in GWT/JSON-RPC
static|static
block|{
name|resultField
operator|=
name|getPrivateField
argument_list|(
name|ActiveCall
operator|.
name|class
argument_list|,
literal|"result"
argument_list|)
expr_stmt|;
name|methodField
operator|=
name|getPrivateField
argument_list|(
name|MethodHandle
operator|.
name|class
argument_list|,
literal|"method"
argument_list|)
expr_stmt|;
block|}
DECL|method|getPrivateField (Class<?> clazz, String fieldName)
specifier|private
specifier|static
name|Field
name|getPrivateField
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
name|Field
name|declaredField
init|=
literal|null
decl_stmt|;
try|try
block|{
name|declaredField
operator|=
name|clazz
operator|.
name|getDeclaredField
argument_list|(
name|fieldName
argument_list|)
expr_stmt|;
name|declaredField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to expose RPS/JSON result field"
argument_list|)
expr_stmt|;
block|}
return|return
name|declaredField
return|;
block|}
comment|// Surrogate of the missing getMethodClass() in GWT/JSON-RPC
DECL|method|getMethodClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getMethodClass
parameter_list|()
block|{
if|if
condition|(
name|methodField
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|Method
name|method
init|=
operator|(
name|Method
operator|)
name|methodField
operator|.
name|get
argument_list|(
name|this
operator|.
name|getMethod
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|method
operator|.
name|getDeclaringClass
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot access result field"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"No permissions to access result field"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|// Surrogate of the missing getResult() in GWT/JSON-RPC
DECL|method|getResult ()
specifier|public
name|Object
name|getResult
parameter_list|()
block|{
if|if
condition|(
name|resultField
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|resultField
operator|.
name|get
argument_list|(
name|this
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot access result field"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"No permissions to access result field"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|GerritCall (final WebSession session, final HttpServletRequest i, final HttpServletResponse o)
name|GerritCall
parameter_list|(
specifier|final
name|WebSession
name|session
parameter_list|,
specifier|final
name|HttpServletRequest
name|i
parameter_list|,
specifier|final
name|HttpServletResponse
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|when
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMethod ()
specifier|public
name|MethodHandle
name|getMethod
parameter_list|()
block|{
if|if
condition|(
name|currentMethod
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|getMethod
argument_list|()
return|;
block|}
return|return
name|currentMethod
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onFailure (final Throwable error)
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|error
parameter_list|)
block|{
if|if
condition|(
name|error
operator|instanceof
name|IllegalArgumentException
operator|||
name|error
operator|instanceof
name|IllegalStateException
condition|)
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|error
operator|instanceof
name|OrmException
operator|||
name|error
operator|instanceof
name|RuntimeException
condition|)
block|{
name|onInternalFailure
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|xsrfValidate ()
specifier|public
name|boolean
name|xsrfValidate
parameter_list|()
block|{
specifier|final
name|String
name|keyIn
init|=
name|getXsrfKeyIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyIn
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|keyIn
argument_list|)
condition|)
block|{
comment|// Anonymous requests don't need XSRF protection, they shouldn't
comment|// be able to cause critical state changes.
comment|//
return|return
operator|!
name|session
operator|.
name|isSignedIn
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|session
operator|.
name|isSignedIn
argument_list|()
operator|&&
name|session
operator|.
name|isValidXGerritAuth
argument_list|(
name|keyIn
argument_list|)
condition|)
block|{
comment|// The session must exist, and must be using this token.
comment|//
name|session
operator|.
name|getUser
argument_list|()
operator|.
name|setAccessPath
argument_list|(
name|AccessPath
operator|.
name|JSON_RPC
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getWebSession ()
specifier|public
name|WebSession
name|getWebSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|getWhen ()
specifier|public
name|long
name|getWhen
parameter_list|()
block|{
return|return
name|when
return|;
block|}
DECL|method|getElapsed ()
specifier|public
name|long
name|getElapsed
parameter_list|()
block|{
return|return
name|TimeUtil
operator|.
name|nowMs
argument_list|()
operator|-
name|when
return|;
block|}
block|}
block|}
end_class

end_unit

