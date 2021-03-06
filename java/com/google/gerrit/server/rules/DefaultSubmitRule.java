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
DECL|package|com.google.gerrit.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rules
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|flogger
operator|.
name|FluentLogger
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
name|data
operator|.
name|LabelFunction
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
name|data
operator|.
name|LabelType
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
name|data
operator|.
name|SubmitRecord
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
name|PatchSetApproval
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
name|exceptions
operator|.
name|StorageException
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
name|annotations
operator|.
name|Exports
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
name|config
operator|.
name|FactoryModule
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectState
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|Collection
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

begin_comment
comment|/**  * Java implementation of Gerrit's default pre-submit rules behavior: check if the labels have the  * correct values, according to the {@link LabelFunction} they are attached to.  *  *<p>As this behavior is also implemented by the Prolog rules system, we skip it if at least one  * project in the hierarchy has a {@code rules.pl} file.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|DefaultSubmitRule
specifier|public
specifier|final
class|class
name|DefaultSubmitRule
implements|implements
name|SubmitRule
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|FactoryModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|SubmitRule
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
literal|"DefaultRules"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultSubmitRule
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|DefaultSubmitRule (ProjectCache projectCache)
name|DefaultSubmitRule
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|evaluate (ChangeData cd)
specifier|public
name|Optional
argument_list|<
name|SubmitRecord
argument_list|>
name|evaluate
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|cd
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
comment|// In case at least one project has a rules.pl file, we let Prolog handle it.
comment|// The Prolog rules engine will also handle the labels for us.
if|if
condition|(
name|projectState
operator|==
literal|null
operator|||
name|projectState
operator|.
name|hasPrologRules
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
name|SubmitRecord
name|submitRecord
init|=
operator|new
name|SubmitRecord
argument_list|()
decl_stmt|;
name|submitRecord
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
expr_stmt|;
name|List
argument_list|<
name|LabelType
argument_list|>
name|labelTypes
decl_stmt|;
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
decl_stmt|;
try|try
block|{
name|labelTypes
operator|=
name|cd
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
expr_stmt|;
name|approvals
operator|=
name|cd
operator|.
name|currentApprovals
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Unable to fetch labels and approvals for change %s"
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|submitRecord
operator|.
name|errorMessage
operator|=
literal|"Unable to fetch labels and approvals for the change"
expr_stmt|;
name|submitRecord
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|RULE_ERROR
expr_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
name|submitRecord
argument_list|)
return|;
block|}
name|submitRecord
operator|.
name|labels
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|labelTypes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|LabelType
name|t
range|:
name|labelTypes
control|)
block|{
name|LabelFunction
name|labelFunction
init|=
name|t
operator|.
name|getFunction
argument_list|()
decl_stmt|;
if|if
condition|(
name|labelFunction
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Unable to find the LabelFunction for label %s, change %s"
argument_list|,
name|t
operator|.
name|getName
argument_list|()
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|submitRecord
operator|.
name|errorMessage
operator|=
literal|"Unable to find the LabelFunction for label "
operator|+
name|t
operator|.
name|getName
argument_list|()
expr_stmt|;
name|submitRecord
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|RULE_ERROR
expr_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
name|submitRecord
argument_list|)
return|;
block|}
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvalsForLabel
init|=
name|getApprovalsForLabel
argument_list|(
name|approvals
argument_list|,
name|t
argument_list|)
decl_stmt|;
name|SubmitRecord
operator|.
name|Label
name|label
init|=
name|labelFunction
operator|.
name|check
argument_list|(
name|t
argument_list|,
name|approvalsForLabel
argument_list|)
decl_stmt|;
name|submitRecord
operator|.
name|labels
operator|.
name|add
argument_list|(
name|label
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|label
operator|.
name|status
condition|)
block|{
case|case
name|OK
case|:
case|case
name|MAY
case|:
break|break;
case|case
name|NEED
case|:
case|case
name|REJECT
case|:
case|case
name|IMPOSSIBLE
case|:
name|submitRecord
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|NOT_READY
expr_stmt|;
break|break;
block|}
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
name|submitRecord
argument_list|)
return|;
block|}
DECL|method|getApprovalsForLabel ( List<PatchSetApproval> approvals, LabelType t)
specifier|private
specifier|static
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|getApprovalsForLabel
parameter_list|(
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
parameter_list|,
name|LabelType
name|t
parameter_list|)
block|{
return|return
name|approvals
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|input
lambda|->
name|input
operator|.
name|label
argument_list|()
operator|.
name|equals
argument_list|(
name|t
operator|.
name|getLabelId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

