begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|entities
operator|.
name|Project
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
name|Optional
import|;
end_import

begin_comment
comment|/** Information about a request that was received from a user. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|RequestInfo
specifier|public
specifier|abstract
class|class
name|RequestInfo
block|{
comment|/** Channel through which a user request was received. */
DECL|enum|RequestType
specifier|public
enum|enum
name|RequestType
block|{
comment|/** request type for git push */
DECL|enumConstant|GIT_RECEIVE
name|GIT_RECEIVE
block|,
comment|/** request type for git fetch */
DECL|enumConstant|GIT_UPLOAD
name|GIT_UPLOAD
block|,
comment|/** request type for call to REST API */
DECL|enumConstant|REST
name|REST
block|,
comment|/** request type for call to SSH API */
DECL|enumConstant|SSH
name|SSH
block|}
comment|/**    * Type of the request, telling through which channel the request was coming in.    *    *<p>See {@link RequestType} for the types that are used by Gerrit core. Other request types are    * possible, e.g. if a plugin supports receiving requests through another channel.    */
DECL|method|requestType ()
specifier|public
specifier|abstract
name|String
name|requestType
parameter_list|()
function_decl|;
comment|/**    * Request URI.    *    *<p>Only set if request type is {@link RequestType#REST}.    *    *<p>Never includes the "/a" prefix.    */
DECL|method|requestUri ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|requestUri
parameter_list|()
function_decl|;
comment|/** The user that has sent the request. */
DECL|method|callingUser ()
specifier|public
specifier|abstract
name|CurrentUser
name|callingUser
parameter_list|()
function_decl|;
comment|/** The trace context of the request. */
DECL|method|traceContext ()
specifier|public
specifier|abstract
name|TraceContext
name|traceContext
parameter_list|()
function_decl|;
comment|/**    * The name of the project for which the request is being done. Only available if the request is    * tied to a project or change. If a project is available it's not guaranteed that it actually    * exists (e.g. if a user made a request for a project that doesn't exist).    */
DECL|method|project ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|project
parameter_list|()
function_decl|;
DECL|method|builder ( RequestType requestType, CurrentUser callingUser, TraceContext traceContext)
specifier|public
specifier|static
name|RequestInfo
operator|.
name|Builder
name|builder
parameter_list|(
name|RequestType
name|requestType
parameter_list|,
name|CurrentUser
name|callingUser
parameter_list|,
name|TraceContext
name|traceContext
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_RequestInfo
operator|.
name|Builder
argument_list|()
operator|.
name|requestType
argument_list|(
name|requestType
argument_list|)
operator|.
name|callingUser
argument_list|(
name|callingUser
argument_list|)
operator|.
name|traceContext
argument_list|(
name|traceContext
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|requestType (String requestType)
specifier|public
specifier|abstract
name|Builder
name|requestType
parameter_list|(
name|String
name|requestType
parameter_list|)
function_decl|;
DECL|method|requestType (RequestType requestType)
specifier|public
name|Builder
name|requestType
parameter_list|(
name|RequestType
name|requestType
parameter_list|)
block|{
return|return
name|requestType
argument_list|(
name|requestType
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|requestUri (String requestUri)
specifier|public
specifier|abstract
name|Builder
name|requestUri
parameter_list|(
name|String
name|requestUri
parameter_list|)
function_decl|;
DECL|method|callingUser (CurrentUser callingUser)
specifier|public
specifier|abstract
name|Builder
name|callingUser
parameter_list|(
name|CurrentUser
name|callingUser
parameter_list|)
function_decl|;
DECL|method|traceContext (TraceContext traceContext)
specifier|public
specifier|abstract
name|Builder
name|traceContext
parameter_list|(
name|TraceContext
name|traceContext
parameter_list|)
function_decl|;
DECL|method|project (Project.NameKey projectName)
specifier|public
specifier|abstract
name|Builder
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|RequestInfo
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

