begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|VersionedMetaData
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
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

begin_comment
comment|/** View of contents at a single ref related to some change. **/
end_comment

begin_class
DECL|class|AbstractChangeNotes
specifier|public
specifier|abstract
class|class
name|AbstractChangeNotes
parameter_list|<
name|T
parameter_list|>
extends|extends
name|VersionedMetaData
block|{
DECL|field|loaded
specifier|private
name|boolean
name|loaded
decl_stmt|;
DECL|field|repoManager
specifier|protected
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|method|AbstractChangeNotes (GitRepositoryManager repoManager, Change change)
name|AbstractChangeNotes
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Change
name|change
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|change
operator|=
operator|new
name|Change
argument_list|(
name|change
argument_list|)
expr_stmt|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|change
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
return|;
block|}
DECL|method|load ()
specifier|public
name|T
name|load
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|loaded
condition|)
block|{
name|Repository
name|repo
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|load
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|loaded
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|self
argument_list|()
return|;
block|}
comment|/**    * @return the NameKey for the project where the notes should be stored,    *    which is not necessarily the same as the change's project.    */
DECL|method|getProjectName ()
specifier|protected
specifier|abstract
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|self ()
specifier|protected
specifier|final
name|T
name|self
parameter_list|()
block|{
return|return
operator|(
name|T
operator|)
name|this
return|;
block|}
block|}
end_class

end_unit

