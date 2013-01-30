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
DECL|package|com.google.gerrit.audit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|Multimap
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
DECL|class|RpcAuditEvent
specifier|public
class|class
name|RpcAuditEvent
extends|extends
name|HttpAuditEvent
block|{
comment|/**    * Creates a new audit event with results    *    * @param sessionId session id the event belongs to    * @param who principal that has generated the event    * @param what object of the event    * @param when time-stamp of when the event started    * @param params parameters of the event    * @param result result of the event    */
DECL|method|RpcAuditEvent (String sessionId, CurrentUser who, String what, long when, Multimap<String, ?> params, String httpMethod, Object input, int status, Object result)
specifier|public
name|RpcAuditEvent
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
name|Multimap
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
name|httpMethod
argument_list|,
name|input
argument_list|,
name|status
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

