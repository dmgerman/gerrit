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
DECL|package|com.google.gerrit.server.query.change
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
name|git
operator|.
name|GitRepositoryManager
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|IncorrectObjectTypeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|MissingObjectException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|filter
operator|.
name|MessageRevFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|filter
operator|.
name|RevFilter
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Predicate to match changes that contains specified text in commit messages  * body.  */
end_comment

begin_class
DECL|class|LegacyMessagePredicate
specifier|public
class|class
name|LegacyMessagePredicate
extends|extends
name|RevWalkPredicate
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
name|LegacyMessagePredicate
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|rFilter
specifier|private
specifier|final
name|RevFilter
name|rFilter
decl_stmt|;
DECL|method|LegacyMessagePredicate (Provider<ReviewDb> db, GitRepositoryManager repoManager, String text)
specifier|public
name|LegacyMessagePredicate
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|super
argument_list|(
name|db
argument_list|,
name|repoManager
argument_list|,
name|ChangeQueryBuilder
operator|.
name|FIELD_MESSAGE
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|this
operator|.
name|rFilter
operator|=
name|MessageRevFilter
operator|.
name|create
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (Repository repo, RevWalk rw, Arguments args)
specifier|public
name|boolean
name|match
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Arguments
name|args
parameter_list|)
block|{
try|try
block|{
return|return
name|rFilter
operator|.
name|include
argument_list|(
name|rw
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|args
operator|.
name|objectId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|args
operator|.
name|projectName
operator|.
name|get
argument_list|()
operator|+
literal|"\" commit does not exist."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|args
operator|.
name|projectName
operator|.
name|get
argument_list|()
operator|+
literal|"\" revision is not a commit."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not search for commit message in \""
operator|+
name|args
operator|.
name|projectName
operator|.
name|get
argument_list|()
operator|+
literal|"\" repository."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

