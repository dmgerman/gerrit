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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|client
operator|.
name|Change
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
name|IdentifiedUser
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
name|config
operator|.
name|ChangeCleanupConfig
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
name|ChangeControl
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
name|NoSuchChangeException
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
name|QueryParseException
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
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
name|QueryProcessor
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
name|Singleton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|AbandonUtil
specifier|public
class|class
name|AbandonUtil
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbandonUtil
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|ChangeCleanupConfig
name|cfg
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|queryProcessor
specifier|private
specifier|final
name|QueryProcessor
name|queryProcessor
decl_stmt|;
DECL|field|queryBuilder
specifier|private
specifier|final
name|ChangeQueryBuilder
name|queryBuilder
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
decl_stmt|;
DECL|field|abandon
specifier|private
specifier|final
name|Abandon
name|abandon
decl_stmt|;
annotation|@
name|Inject
DECL|method|AbandonUtil ( ChangeCleanupConfig cfg, IdentifiedUser.GenericFactory identifiedUserFactory, QueryProcessor queryProcessor, ChangeQueryBuilder queryBuilder, ChangeControl.GenericFactory changeControlFactory, Abandon abandon)
name|AbandonUtil
parameter_list|(
name|ChangeCleanupConfig
name|cfg
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
name|QueryProcessor
name|queryProcessor
parameter_list|,
name|ChangeQueryBuilder
name|queryBuilder
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
parameter_list|,
name|Abandon
name|abandon
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|queryProcessor
operator|=
name|queryProcessor
expr_stmt|;
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|abandon
operator|=
name|abandon
expr_stmt|;
block|}
DECL|method|abandonInactiveOpenChanges ()
specifier|public
name|void
name|abandonInactiveOpenChanges
parameter_list|()
block|{
if|if
condition|(
name|cfg
operator|.
name|getAbandonAfter
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
try|try
block|{
name|String
name|query
init|=
literal|"status:new age:"
operator|+
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toMinutes
argument_list|(
name|cfg
operator|.
name|getAbandonAfter
argument_list|()
argument_list|)
operator|+
literal|"m"
decl_stmt|;
if|if
condition|(
operator|!
name|cfg
operator|.
name|getAbandonIfMergeable
argument_list|()
condition|)
block|{
name|query
operator|+=
literal|" -is:mergeable"
expr_stmt|;
block|}
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changesToAbandon
init|=
name|queryProcessor
operator|.
name|enforceVisibility
argument_list|(
literal|false
argument_list|)
operator|.
name|queryChanges
argument_list|(
name|queryBuilder
operator|.
name|parse
argument_list|(
name|query
argument_list|)
argument_list|)
operator|.
name|changes
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changesToAbandon
control|)
block|{
try|try
block|{
if|if
condition|(
name|noNeedToAbandon
argument_list|(
name|cd
argument_list|,
name|query
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Change data \"{}\" does not satisfy the query \"{}\" any"
operator|+
literal|" more, hence skipping it in clean up"
argument_list|,
name|cd
argument_list|,
name|query
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|abandon
operator|.
name|abandon
argument_list|(
name|changeControl
argument_list|(
name|cd
argument_list|)
argument_list|,
name|cfg
operator|.
name|getAbandonMessage
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
comment|// Change was already merged or abandoned.
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to auto-abandon inactive open change %d."
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Auto-Abandoned %d of %d changes."
argument_list|,
name|count
argument_list|,
name|changesToAbandon
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
decl||
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to query inactive open changes for auto-abandoning."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|noNeedToAbandon (ChangeData cd, String query)
specifier|private
name|boolean
name|noNeedToAbandon
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
name|String
name|newQuery
init|=
name|query
operator|+
literal|" change:"
operator|+
name|cd
operator|.
name|getId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changesToAbandon
init|=
name|queryProcessor
operator|.
name|enforceVisibility
argument_list|(
literal|false
argument_list|)
operator|.
name|queryChanges
argument_list|(
name|queryBuilder
operator|.
name|parse
argument_list|(
name|newQuery
argument_list|)
argument_list|)
operator|.
name|changes
argument_list|()
decl_stmt|;
return|return
name|changesToAbandon
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|changeControl (ChangeData cd)
specifier|private
name|ChangeControl
name|changeControl
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|OrmException
block|{
name|Change
name|c
init|=
name|cd
operator|.
name|change
argument_list|()
decl_stmt|;
return|return
name|changeControlFactory
operator|.
name|controlFor
argument_list|(
name|c
argument_list|,
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

