begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|server
operator|.
name|AuditEvent
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

begin_class
DECL|class|HttpAuditEvent
specifier|public
class|class
name|HttpAuditEvent
extends|extends
name|AuditEvent
block|{
DECL|field|httpMethod
specifier|public
specifier|final
name|String
name|httpMethod
decl_stmt|;
DECL|field|httpStatus
specifier|public
specifier|final
name|int
name|httpStatus
decl_stmt|;
DECL|field|input
specifier|public
specifier|final
name|Object
name|input
decl_stmt|;
comment|/**    * Creates a new audit event with results    *    * @param sessionId session id the event belongs to    * @param who principal that has generated the event    * @param what object of the event    * @param when time-stamp of when the event started    * @param params parameters of the event    * @param httpMethod HTTP method    * @param input input    * @param status HTTP status    * @param result result of the event    */
DECL|method|HttpAuditEvent ( String sessionId, CurrentUser who, String what, long when, ListMultimap<String, ?> params, String httpMethod, Object input, int status, Object result)
specifier|public
name|HttpAuditEvent
parameter_list|(
name|String
name|sessionId
parameter_list|,
name|CurrentUser
name|who
parameter_list|,
name|String
name|what
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
name|String
name|httpMethod
parameter_list|,
name|Object
name|input
parameter_list|,
name|int
name|status
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|sessionId
argument_list|,
name|who
argument_list|,
name|what
argument_list|,
name|when
argument_list|,
name|params
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpMethod
operator|=
name|httpMethod
expr_stmt|;
name|this
operator|.
name|input
operator|=
name|input
expr_stmt|;
name|this
operator|.
name|httpStatus
operator|=
name|status
expr_stmt|;
block|}
block|}
end_class

end_unit

