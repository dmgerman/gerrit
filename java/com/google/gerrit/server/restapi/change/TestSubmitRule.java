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
DECL|package|com.google.gerrit.server.restapi.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|change
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
name|MoreObjects
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
name|extensions
operator|.
name|common
operator|.
name|AccountInfo
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
name|common
operator|.
name|TestSubmitRuleInfo
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
name|common
operator|.
name|TestSubmitRuleInput
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
name|common
operator|.
name|TestSubmitRuleInput
operator|.
name|Filters
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
name|AuthException
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
name|BadRequestException
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
name|Response
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
name|RestModifyView
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
name|account
operator|.
name|AccountLoader
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
name|change
operator|.
name|RevisionResource
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
name|permissions
operator|.
name|PermissionBackendException
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
name|gerrit
operator|.
name|server
operator|.
name|rules
operator|.
name|PrologOptions
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
name|rules
operator|.
name|PrologRule
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
name|rules
operator|.
name|RulesCache
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
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|TestSubmitRule
specifier|public
class|class
name|TestSubmitRule
implements|implements
name|RestModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|TestSubmitRuleInput
argument_list|>
block|{
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|rules
specifier|private
specifier|final
name|RulesCache
name|rules
decl_stmt|;
DECL|field|accountInfoFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountInfoFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|prologRule
specifier|private
specifier|final
name|PrologRule
name|prologRule
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--filters"
argument_list|,
name|usage
operator|=
literal|"impact of filters in parent projects"
argument_list|)
DECL|field|filters
specifier|private
name|Filters
name|filters
init|=
name|Filters
operator|.
name|RUN
decl_stmt|;
annotation|@
name|Inject
DECL|method|TestSubmitRule ( ChangeData.Factory changeDataFactory, RulesCache rules, AccountLoader.Factory infoFactory, ProjectCache projectCache, PrologRule prologRule)
name|TestSubmitRule
parameter_list|(
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|RulesCache
name|rules
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|infoFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|PrologRule
name|prologRule
parameter_list|)
block|{
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|rules
operator|=
name|rules
expr_stmt|;
name|this
operator|.
name|accountInfoFactory
operator|=
name|infoFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|prologRule
operator|=
name|prologRule
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc, TestSubmitRuleInput input)
specifier|public
name|Response
argument_list|<
name|TestSubmitRuleInfo
argument_list|>
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|TestSubmitRuleInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
throws|,
name|BadRequestException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|TestSubmitRuleInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|rule
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"rule is required"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|rules
operator|.
name|isProjectRulesEnabled
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"project rules are disabled"
argument_list|)
throw|;
block|}
name|input
operator|.
name|filters
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|filters
argument_list|,
name|filters
argument_list|)
expr_stmt|;
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"project not found"
argument_list|)
throw|;
block|}
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
name|SubmitRecord
name|record
init|=
name|prologRule
operator|.
name|evaluate
argument_list|(
name|cd
argument_list|,
name|PrologOptions
operator|.
name|dryRunOptions
argument_list|(
name|input
operator|.
name|rule
argument_list|,
name|input
operator|.
name|filters
operator|==
name|Filters
operator|.
name|SKIP
argument_list|)
argument_list|)
decl_stmt|;
name|AccountLoader
name|accounts
init|=
name|accountInfoFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|TestSubmitRuleInfo
name|out
init|=
name|newSubmitRuleInfo
argument_list|(
name|record
argument_list|,
name|accounts
argument_list|)
decl_stmt|;
name|accounts
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|out
argument_list|)
return|;
block|}
DECL|method|newSubmitRuleInfo (SubmitRecord r, AccountLoader accounts)
specifier|private
specifier|static
name|TestSubmitRuleInfo
name|newSubmitRuleInfo
parameter_list|(
name|SubmitRecord
name|r
parameter_list|,
name|AccountLoader
name|accounts
parameter_list|)
block|{
name|TestSubmitRuleInfo
name|info
init|=
operator|new
name|TestSubmitRuleInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|status
operator|=
name|r
operator|.
name|status
operator|.
name|name
argument_list|()
expr_stmt|;
name|info
operator|.
name|errorMessage
operator|=
name|r
operator|.
name|errorMessage
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|labels
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SubmitRecord
operator|.
name|Label
name|n
range|:
name|r
operator|.
name|labels
control|)
block|{
name|AccountInfo
name|who
init|=
name|n
operator|.
name|appliedBy
operator|!=
literal|null
condition|?
name|accounts
operator|.
name|get
argument_list|(
name|n
operator|.
name|appliedBy
argument_list|)
else|:
operator|new
name|AccountInfo
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|label
argument_list|(
name|info
argument_list|,
name|n
argument_list|,
name|who
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|info
return|;
block|}
DECL|method|label (TestSubmitRuleInfo info, SubmitRecord.Label n, AccountInfo who)
specifier|private
specifier|static
name|void
name|label
parameter_list|(
name|TestSubmitRuleInfo
name|info
parameter_list|,
name|SubmitRecord
operator|.
name|Label
name|n
parameter_list|,
name|AccountInfo
name|who
parameter_list|)
block|{
switch|switch
condition|(
name|n
operator|.
name|status
condition|)
block|{
case|case
name|OK
case|:
if|if
condition|(
name|info
operator|.
name|ok
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|ok
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|info
operator|.
name|ok
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
name|who
argument_list|)
expr_stmt|;
break|break;
case|case
name|REJECT
case|:
if|if
condition|(
name|info
operator|.
name|reject
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|reject
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|info
operator|.
name|reject
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
name|who
argument_list|)
expr_stmt|;
break|break;
case|case
name|NEED
case|:
if|if
condition|(
name|info
operator|.
name|need
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|need
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|info
operator|.
name|need
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
name|TestSubmitRuleInfo
operator|.
name|None
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
break|break;
case|case
name|MAY
case|:
if|if
condition|(
name|info
operator|.
name|may
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|may
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|info
operator|.
name|may
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
name|who
argument_list|)
expr_stmt|;
break|break;
case|case
name|IMPOSSIBLE
case|:
if|if
condition|(
name|info
operator|.
name|impossible
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|impossible
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|info
operator|.
name|impossible
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
name|TestSubmitRuleInfo
operator|.
name|None
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

