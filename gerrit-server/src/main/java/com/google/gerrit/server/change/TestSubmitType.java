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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|extensions
operator|.
name|common
operator|.
name|SubmitType
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|TestSubmitRule
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
name|server
operator|.
name|change
operator|.
name|TestSubmitRule
operator|.
name|Input
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
name|RuleEvalException
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
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|SymbolTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Term
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
DECL|class|TestSubmitType
specifier|public
class|class
name|TestSubmitType
implements|implements
name|RestModifyView
argument_list|<
name|RevisionResource
argument_list|,
name|Input
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
DECL|method|TestSubmitType (Provider<ReviewDb> db, ChangeData.Factory changeDataFactory, RulesCache rules)
name|TestSubmitType
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
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource rsrc, Input input)
specifier|public
name|SubmitType
name|apply
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
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
name|Input
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
name|SubmitRuleEvaluator
name|evaluator
init|=
operator|new
name|SubmitRuleEvaluator
argument_list|(
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
name|getControl
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Term
argument_list|>
name|results
decl_stmt|;
try|try
block|{
name|results
operator|=
name|evaluator
operator|.
name|setPatchSet
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|)
operator|.
name|setSkipSubmitFilters
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
name|setRule
argument_list|(
name|input
operator|.
name|rule
argument_list|)
operator|.
name|evaluateSubmitType
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuleEvalException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"rule failed with exception: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"rule %s has no solution"
argument_list|,
name|evaluator
operator|.
name|getSubmitRule
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|Term
name|type
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|type
operator|.
name|isSymbol
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"rule %s produced invalid result: %s"
argument_list|,
name|evaluator
operator|.
name|getSubmitRule
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|type
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|typeName
init|=
operator|(
operator|(
name|SymbolTerm
operator|)
name|type
operator|)
operator|.
name|name
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|SubmitType
operator|.
name|valueOf
argument_list|(
name|typeName
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"rule %s produced invalid result: %s"
argument_list|,
name|evaluator
operator|.
name|getSubmitRule
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|type
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|class|Get
specifier|static
class|class
name|Get
implements|implements
name|RestReadView
argument_list|<
name|RevisionResource
argument_list|>
block|{
DECL|field|test
specifier|private
specifier|final
name|TestSubmitType
name|test
decl_stmt|;
annotation|@
name|Inject
DECL|method|Get (TestSubmitType test)
name|Get
parameter_list|(
name|TestSubmitType
name|test
parameter_list|)
block|{
name|this
operator|.
name|test
operator|=
name|test
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RevisionResource resource)
specifier|public
name|SubmitType
name|apply
parameter_list|(
name|RevisionResource
name|resource
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|OrmException
block|{
return|return
name|test
operator|.
name|apply
argument_list|(
name|resource
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

