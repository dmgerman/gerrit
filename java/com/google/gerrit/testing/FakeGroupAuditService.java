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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
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
name|audit
operator|.
name|AuditListener
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
name|server
operator|.
name|audit
operator|.
name|group
operator|.
name|GroupAuditListener
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
name|group
operator|.
name|GroupAuditService
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
name|plugincontext
operator|.
name|PluginSetContext
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
name|AbstractModule
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_class
annotation|@
name|Singleton
DECL|class|FakeGroupAuditService
specifier|public
class|class
name|FakeGroupAuditService
extends|extends
name|AuditService
block|{
DECL|field|auditEvents
specifier|public
specifier|final
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|auditEvents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|GroupAuditListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|AuditListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|GroupAuditService
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|FakeGroupAuditService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
DECL|method|FakeGroupAuditService ( PluginSetContext<AuditListener> auditListeners, PluginSetContext<GroupAuditListener> groupAuditListeners)
name|FakeGroupAuditService
parameter_list|(
name|PluginSetContext
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
parameter_list|,
name|PluginSetContext
argument_list|<
name|GroupAuditListener
argument_list|>
name|groupAuditListeners
parameter_list|)
block|{
name|super
argument_list|(
name|auditListeners
argument_list|,
name|groupAuditListeners
argument_list|)
expr_stmt|;
block|}
DECL|method|clearEvents ()
specifier|public
name|void
name|clearEvents
parameter_list|()
block|{
name|auditEvents
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|dispatch (AuditEvent action)
specifier|public
name|void
name|dispatch
parameter_list|(
name|AuditEvent
name|action
parameter_list|)
block|{
name|super
operator|.
name|dispatch
argument_list|(
name|action
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|auditEvents
init|)
block|{
name|auditEvents
operator|.
name|add
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|auditEvents
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

