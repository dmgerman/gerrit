begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.audit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|audit
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
name|base
operator|.
name|Preconditions
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
name|ListMultimap
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
name|restapi
operator|.
name|RestResource
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
name|restapi
operator|.
name|RestView
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/** Extended audit event. Adds request, resource and view data to HttpAuditEvent. */
end_comment

begin_class
DECL|class|ExtendedHttpAuditEvent
specifier|public
class|class
name|ExtendedHttpAuditEvent
extends|extends
name|HttpAuditEvent
block|{
DECL|field|httpRequest
specifier|public
specifier|final
name|HttpServletRequest
name|httpRequest
decl_stmt|;
DECL|field|resource
specifier|public
specifier|final
name|RestResource
name|resource
decl_stmt|;
DECL|field|view
specifier|public
specifier|final
name|RestView
argument_list|<
name|?
extends|extends
name|RestResource
argument_list|>
name|view
decl_stmt|;
comment|/**    * Creates a new audit event with results    *    * @param sessionId session id the event belongs to    * @param who principal that has generated the event    * @param httpRequest the HttpServletRequest    * @param when time-stamp of when the event started    * @param params parameters of the event    * @param input input    * @param status HTTP status    * @param result result of the event    * @param resource REST resource data    * @param view view rendering object    */
DECL|method|ExtendedHttpAuditEvent ( String sessionId, CurrentUser who, HttpServletRequest httpRequest, long when, ListMultimap<String, ?> params, Object input, int status, Object result, RestResource resource, RestView<RestResource> view)
specifier|public
name|ExtendedHttpAuditEvent
parameter_list|(
name|String
name|sessionId
parameter_list|,
name|CurrentUser
name|who
parameter_list|,
name|HttpServletRequest
name|httpRequest
parameter_list|,
name|long
name|when
parameter_list|,
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|params
parameter_list|,
name|Object
name|input
parameter_list|,
name|int
name|status
parameter_list|,
name|Object
name|result
parameter_list|,
name|RestResource
name|resource
parameter_list|,
name|RestView
argument_list|<
name|RestResource
argument_list|>
name|view
parameter_list|)
block|{
name|super
argument_list|(
name|sessionId
argument_list|,
name|who
argument_list|,
name|httpRequest
operator|.
name|getRequestURI
argument_list|()
argument_list|,
name|when
argument_list|,
name|params
argument_list|,
name|httpRequest
operator|.
name|getMethod
argument_list|()
argument_list|,
name|input
argument_list|,
name|status
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpRequest
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|httpRequest
argument_list|)
expr_stmt|;
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
name|this
operator|.
name|view
operator|=
name|view
expr_stmt|;
block|}
block|}
end_class

end_unit

