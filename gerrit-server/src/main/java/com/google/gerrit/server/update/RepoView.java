begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|checkArgument
import|;
end_import

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
name|checkNotNull
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
name|Project
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|ObjectId
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
name|ObjectInserter
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

begin_class
DECL|class|RepoView
specifier|public
class|class
name|RepoView
block|{
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|rw
specifier|private
specifier|final
name|RevWalk
name|rw
decl_stmt|;
DECL|field|inserter
specifier|private
specifier|final
name|ObjectInserter
name|inserter
decl_stmt|;
DECL|field|commands
specifier|private
specifier|final
name|ChainedReceiveCommands
name|commands
decl_stmt|;
DECL|field|closeRepo
specifier|private
specifier|final
name|boolean
name|closeRepo
decl_stmt|;
DECL|method|RepoView (GitRepositoryManager repoManager, Project.NameKey project)
name|RepoView
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|IOException
block|{
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|inserter
operator|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
expr_stmt|;
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|inserter
operator|.
name|newReader
argument_list|()
argument_list|)
expr_stmt|;
name|commands
operator|=
operator|new
name|ChainedReceiveCommands
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|closeRepo
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|RepoView (Repository repo, RevWalk rw, ObjectInserter inserter)
name|RepoView
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
operator|.
name|getCreatedFromInserter
argument_list|()
operator|==
name|inserter
argument_list|,
literal|"expected RevWalk %s to be created by ObjectInserter %s"
argument_list|,
name|rw
argument_list|,
name|inserter
argument_list|)
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|checkNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|checkNotNull
argument_list|(
name|rw
argument_list|)
expr_stmt|;
name|this
operator|.
name|inserter
operator|=
name|checkNotNull
argument_list|(
name|inserter
argument_list|)
expr_stmt|;
name|commands
operator|=
operator|new
name|ChainedReceiveCommands
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|closeRepo
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|getRevWalk ()
specifier|public
name|RevWalk
name|getRevWalk
parameter_list|()
block|{
return|return
name|rw
return|;
block|}
DECL|method|getInserter ()
specifier|public
name|ObjectInserter
name|getInserter
parameter_list|()
block|{
return|return
name|inserter
return|;
block|}
DECL|method|getCommands ()
specifier|public
name|ChainedReceiveCommands
name|getCommands
parameter_list|()
block|{
return|return
name|commands
return|;
block|}
DECL|method|getRef (String name)
specifier|public
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|getRef
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getCommands
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|// TODO(dborowitz): Remove this so callers can't do arbitrary stuff.
DECL|method|getRepository ()
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repo
return|;
block|}
comment|// Not AutoCloseable so callers can't improperly close it. Plus it's never managed with a try
comment|// block anyway.
DECL|method|close ()
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|closeRepo
condition|)
block|{
name|inserter
operator|.
name|close
argument_list|()
expr_stmt|;
name|rw
operator|.
name|close
argument_list|()
expr_stmt|;
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

