begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|project
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|Branch
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
name|reviewdb
operator|.
name|RevId
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|NoSuchProjectException
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
name|ProjectControl
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
name|assistedinject
operator|.
name|Assisted
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
name|RepositoryNotFoundException
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
name|Constants
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
name|Ref
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_class
DECL|class|ListBranches
class|class
name|ListBranches
extends|extends
name|Handler
argument_list|<
name|List
argument_list|<
name|Branch
argument_list|>
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (@ssisted Project.NameKey name)
name|ListBranches
name|create
parameter_list|(
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
function_decl|;
block|}
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|projectName
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListBranches (final ProjectControl.Factory projectControlFactory, final GitRepositoryManager repoManager, @Assisted final Project.NameKey name)
name|ListBranches
parameter_list|(
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
specifier|final
name|GitRepositoryManager
name|repoManager
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|projectName
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|List
argument_list|<
name|Branch
argument_list|>
name|call
parameter_list|()
throws|throws
name|NoSuchProjectException
throws|,
name|RepositoryNotFoundException
block|{
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
name|projectName
argument_list|,
name|ProjectControl
operator|.
name|OWNER
operator||
name|ProjectControl
operator|.
name|VISIBLE
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Branch
argument_list|>
name|branches
init|=
operator|new
name|ArrayList
argument_list|<
name|Branch
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Repository
name|db
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|all
init|=
name|db
operator|.
name|getAllRefs
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|all
operator|.
name|containsKey
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
condition|)
block|{
comment|// The branch pointed to by HEAD doesn't exist yet. Fake
comment|// that it exists by returning a Ref with no ObjectId.
comment|//
try|try
block|{
specifier|final
name|String
name|head
init|=
name|db
operator|.
name|getFullBranch
argument_list|()
decl_stmt|;
if|if
condition|(
name|head
operator|!=
literal|null
operator|&&
name|head
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_REFS
argument_list|)
condition|)
block|{
name|all
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|,
operator|new
name|Ref
argument_list|(
name|Ref
operator|.
name|Storage
operator|.
name|LOOSE
argument_list|,
name|Constants
operator|.
name|HEAD
argument_list|,
name|head
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore the failure reading HEAD.
block|}
block|}
for|for
control|(
specifier|final
name|Ref
name|ref
range|:
name|all
operator|.
name|values
argument_list|()
control|)
block|{
specifier|final
name|String
name|name
init|=
name|ref
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
condition|)
block|{
specifier|final
name|Branch
name|b
init|=
operator|new
name|Branch
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|setRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|branches
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|branches
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Branch
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|Branch
name|a
parameter_list|,
specifier|final
name|Branch
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|branches
return|;
block|}
block|}
end_class

end_unit

