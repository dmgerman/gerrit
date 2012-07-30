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
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|AuditService
specifier|public
class|class
name|AuditService
block|{
DECL|field|auditListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
decl_stmt|;
annotation|@
name|Inject
DECL|method|AuditService (DynamicSet<AuditListener> auditListeners)
specifier|public
name|AuditService
parameter_list|(
name|DynamicSet
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|=
name|auditListeners
expr_stmt|;
block|}
DECL|method|dispatch (AuditEvent action)
specifier|public
name|void
name|dispatch
parameter_list|(
name|AuditEvent
name|action
parameter_list|)
block|{
for|for
control|(
name|AuditListener
name|auditListener
range|:
name|auditListeners
control|)
block|{
name|auditListener
operator|.
name|onAuditableAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

