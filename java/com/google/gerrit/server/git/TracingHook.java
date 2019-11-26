begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

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
name|logging
operator|.
name|TraceContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|FetchV2Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|LsRefsV2Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ProtocolV2Hook
import|;
end_import

begin_comment
comment|/**  * Git hook for ls-refs and fetch that enables Gerrit request tracing if the user sets the 'trace'  * server option.  *  *<p>This hook is only invoked if Git protocol v2 is used.  *  *<p>If the 'trace' server option is specified without value, this means without providing a trace  * ID, a trace ID is generated, but it's not returned to the client. Hence users are advised to  * always provide a trace ID.  */
end_comment

begin_class
DECL|class|TracingHook
specifier|public
class|class
name|TracingHook
implements|implements
name|ProtocolV2Hook
implements|,
name|AutoCloseable
block|{
DECL|field|traceContext
specifier|private
name|TraceContext
name|traceContext
decl_stmt|;
annotation|@
name|Override
DECL|method|onLsRefs (LsRefsV2Request req)
specifier|public
name|void
name|onLsRefs
parameter_list|(
name|LsRefsV2Request
name|req
parameter_list|)
block|{
name|maybeStartTrace
argument_list|(
name|req
operator|.
name|getServerOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFetch (FetchV2Request req)
specifier|public
name|void
name|onFetch
parameter_list|(
name|FetchV2Request
name|req
parameter_list|)
block|{
name|maybeStartTrace
argument_list|(
name|req
operator|.
name|getServerOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|traceContext
operator|!=
literal|null
condition|)
block|{
name|traceContext
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Starts request tracing if 'trace' server option is set.    *    * @param serverOptionList list of provided server options    */
DECL|method|maybeStartTrace (List<String> serverOptionList)
specifier|private
name|void
name|maybeStartTrace
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|serverOptionList
parameter_list|)
block|{
if|if
condition|(
name|traceContext
operator|!=
literal|null
condition|)
block|{
comment|// Trace was already started
return|return;
block|}
name|Optional
argument_list|<
name|String
argument_list|>
name|traceOption
init|=
name|parseTraceOption
argument_list|(
name|serverOptionList
argument_list|)
decl_stmt|;
name|traceContext
operator|=
name|TraceContext
operator|.
name|newTrace
argument_list|(
name|traceOption
operator|.
name|isPresent
argument_list|()
argument_list|,
name|traceOption
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|,
parameter_list|(
name|tagName
parameter_list|,
name|traceId
parameter_list|)
lambda|->
block|{
comment|// TODO(ekempin): Return trace ID to client
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|parseTraceOption (List<String> serverOptionList)
specifier|private
name|Optional
argument_list|<
name|String
argument_list|>
name|parseTraceOption
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|serverOptionList
parameter_list|)
block|{
if|if
condition|(
name|serverOptionList
operator|==
literal|null
operator|||
name|serverOptionList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|Optional
argument_list|<
name|String
argument_list|>
name|traceOption
init|=
name|serverOptionList
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|o
lambda|->
name|o
operator|.
name|startsWith
argument_list|(
literal|"trace"
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|traceOption
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
name|int
name|e
init|=
name|traceOption
operator|.
name|get
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|>
literal|0
condition|)
block|{
comment|// trace option was specified with trace ID: "--trace=<trace-ID>"
return|return
name|Optional
operator|.
name|of
argument_list|(
name|traceOption
operator|.
name|get
argument_list|()
operator|.
name|substring
argument_list|(
name|e
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
comment|// trace option was specified without trace ID: "--trace",
comment|// return an empty string so that a trace ID is generated
return|return
name|Optional
operator|.
name|of
argument_list|(
literal|""
argument_list|)
return|;
block|}
block|}
end_class

end_unit

