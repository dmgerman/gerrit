begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.account
package|package
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
name|account
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
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
name|account
operator|.
name|AccountQueryBuilder
operator|.
name|FIELD_LIMIT
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
name|index
operator|.
name|IndexConfig
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
name|index
operator|.
name|query
operator|.
name|AndSource
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
name|index
operator|.
name|query
operator|.
name|IndexPredicate
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|index
operator|.
name|query
operator|.
name|QueryProcessor
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
name|metrics
operator|.
name|MetricMaker
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
name|AccountControl
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
name|AccountLimits
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
name|AccountState
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
name|index
operator|.
name|account
operator|.
name|AccountIndexCollection
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
name|index
operator|.
name|account
operator|.
name|AccountIndexRewriter
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
name|index
operator|.
name|account
operator|.
name|AccountSchemaDefinitions
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

begin_comment
comment|/**  * Query processor for the account index.  *  *<p>Instances are one-time-use. Other singleton classes should inject a Provider rather than  * holding on to a single instance.  */
end_comment

begin_class
DECL|class|AccountQueryProcessor
specifier|public
class|class
name|AccountQueryProcessor
extends|extends
name|QueryProcessor
argument_list|<
name|AccountState
argument_list|>
block|{
DECL|field|accountControlFactory
specifier|private
specifier|final
name|AccountControl
operator|.
name|Factory
name|accountControlFactory
decl_stmt|;
static|static
block|{
comment|// It is assumed that basic rewrites do not touch visibleto predicates.
name|checkState
argument_list|(
operator|!
name|AccountIsVisibleToPredicate
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|IndexPredicate
operator|.
name|class
argument_list|)
argument_list|,
literal|"AccountQueryProcessor assumes visibleto is not used by the index rewriter."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|AccountQueryProcessor ( Provider<CurrentUser> userProvider, AccountLimits.Factory limitsFactory, MetricMaker metricMaker, IndexConfig indexConfig, AccountIndexCollection indexes, AccountIndexRewriter rewriter, AccountControl.Factory accountControlFactory)
specifier|protected
name|AccountQueryProcessor
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|AccountLimits
operator|.
name|Factory
name|limitsFactory
parameter_list|,
name|MetricMaker
name|metricMaker
parameter_list|,
name|IndexConfig
name|indexConfig
parameter_list|,
name|AccountIndexCollection
name|indexes
parameter_list|,
name|AccountIndexRewriter
name|rewriter
parameter_list|,
name|AccountControl
operator|.
name|Factory
name|accountControlFactory
parameter_list|)
block|{
name|super
argument_list|(
name|metricMaker
argument_list|,
name|AccountSchemaDefinitions
operator|.
name|INSTANCE
argument_list|,
name|indexConfig
argument_list|,
name|indexes
argument_list|,
name|rewriter
argument_list|,
name|FIELD_LIMIT
argument_list|,
parameter_list|()
lambda|->
name|limitsFactory
operator|.
name|create
argument_list|(
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getQueryLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountControlFactory
operator|=
name|accountControlFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|enforceVisibility (Predicate<AccountState> pred)
specifier|protected
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|enforceVisibility
parameter_list|(
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|pred
parameter_list|)
block|{
return|return
operator|new
name|AndSource
argument_list|<>
argument_list|(
name|pred
argument_list|,
operator|new
name|AccountIsVisibleToPredicate
argument_list|(
name|accountControlFactory
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|start
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|formatForLogging (AccountState accountState)
specifier|protected
name|String
name|formatForLogging
parameter_list|(
name|AccountState
name|accountState
parameter_list|)
block|{
return|return
name|accountState
operator|.
name|account
argument_list|()
operator|.
name|id
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

