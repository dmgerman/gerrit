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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|project
operator|.
name|SubmitRuleEvaluator
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
name|SubmitRuleOptions
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
name|RulesCache
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|Map
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
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
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
DECL|field|submitRuleEvaluatorFactory
specifier|private
specifier|final
name|SubmitRuleEvaluator
operator|.
name|Factory
name|submitRuleEvaluatorFactory
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
DECL|method|TestSubmitRule ( Provider<ReviewDb> db, ChangeData.Factory changeDataFactory, RulesCache rules, AccountLoader.Factory infoFactory, SubmitRuleEvaluator.Factory submitRuleEvaluatorFactory)
name|TestSubmitRule
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
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
name|SubmitRuleEvaluator
operator|.
name|Factory
name|submitRuleEvaluatorFactory
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
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
name|submitRuleEvaluatorFactory
operator|=
name|submitRuleEvaluatorFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc, TestSubmitRuleInput input)
specifier|public
name|List
argument_list|<
name|Record
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
name|OrmException
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
operator|!=
literal|null
operator|&&
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
name|SubmitRuleOptions
name|opts
init|=
name|SubmitRuleOptions
operator|.
name|builder
argument_list|()
operator|.
name|skipFilters
argument_list|(
name|input
operator|.
name|filters
operator|==
name|Filters
operator|.
name|SKIP
argument_list|)
operator|.
name|rule
argument_list|(
name|input
operator|.
name|rule
argument_list|)
operator|.
name|logErrors
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|records
init|=
name|submitRuleEvaluatorFactory
operator|.
name|create
argument_list|(
name|opts
argument_list|)
operator|.
name|evaluate
argument_list|(
name|cd
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Record
argument_list|>
name|out
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|records
operator|.
name|size
argument_list|()
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
for|for
control|(
name|SubmitRecord
name|r
range|:
name|records
control|)
block|{
name|out
operator|.
name|add
argument_list|(
operator|new
name|Record
argument_list|(
name|r
argument_list|,
name|accounts
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|accounts
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|out
return|;
block|}
DECL|class|Record
specifier|static
class|class
name|Record
block|{
DECL|field|status
name|SubmitRecord
operator|.
name|Status
name|status
decl_stmt|;
DECL|field|errorMessage
name|String
name|errorMessage
decl_stmt|;
DECL|field|ok
name|Map
argument_list|<
name|String
argument_list|,
name|AccountInfo
argument_list|>
name|ok
decl_stmt|;
DECL|field|reject
name|Map
argument_list|<
name|String
argument_list|,
name|AccountInfo
argument_list|>
name|reject
decl_stmt|;
DECL|field|need
name|Map
argument_list|<
name|String
argument_list|,
name|None
argument_list|>
name|need
decl_stmt|;
DECL|field|may
name|Map
argument_list|<
name|String
argument_list|,
name|AccountInfo
argument_list|>
name|may
decl_stmt|;
DECL|field|impossible
name|Map
argument_list|<
name|String
argument_list|,
name|None
argument_list|>
name|impossible
decl_stmt|;
DECL|method|Record (SubmitRecord r, AccountLoader accounts)
name|Record
parameter_list|(
name|SubmitRecord
name|r
parameter_list|,
name|AccountLoader
name|accounts
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|r
operator|.
name|status
expr_stmt|;
name|this
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
name|n
argument_list|,
name|who
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|label (SubmitRecord.Label n, AccountInfo who)
specifier|private
name|void
name|label
parameter_list|(
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
name|ok
operator|==
literal|null
condition|)
block|{
name|ok
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
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
name|reject
operator|==
literal|null
condition|)
block|{
name|reject
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
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
name|need
operator|==
literal|null
condition|)
block|{
name|need
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|need
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
operator|new
name|None
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|MAY
case|:
if|if
condition|(
name|may
operator|==
literal|null
condition|)
block|{
name|may
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
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
name|impossible
operator|==
literal|null
condition|)
block|{
name|impossible
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|impossible
operator|.
name|put
argument_list|(
name|n
operator|.
name|label
argument_list|,
operator|new
name|None
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
DECL|class|None
specifier|static
class|class
name|None
block|{}
block|}
end_class

end_unit

