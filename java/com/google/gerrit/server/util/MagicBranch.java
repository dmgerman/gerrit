begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|Capable
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
name|List
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

begin_class
DECL|class|MagicBranch
specifier|public
specifier|final
class|class
name|MagicBranch
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
DECL|field|NEW_CHANGE
specifier|public
specifier|static
specifier|final
name|String
name|NEW_CHANGE
init|=
literal|"refs/for/"
decl_stmt|;
comment|/** Extracts the destination from a ref name */
DECL|method|getDestBranchName (String refName)
specifier|public
specifier|static
name|String
name|getDestBranchName
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
return|return
name|refName
operator|.
name|substring
argument_list|(
name|NEW_CHANGE
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
comment|/** Checks if the supplied ref name is a magic branch */
DECL|method|isMagicBranch (String refName)
specifier|public
specifier|static
name|boolean
name|isMagicBranch
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
return|return
name|refName
operator|.
name|startsWith
argument_list|(
name|NEW_CHANGE
argument_list|)
return|;
block|}
comment|/** Returns the ref name prefix for a magic branch, {@code null} if the branch is not magic */
DECL|method|getMagicRefNamePrefix (String refName)
specifier|public
specifier|static
name|String
name|getMagicRefNamePrefix
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
if|if
condition|(
name|refName
operator|.
name|startsWith
argument_list|(
name|NEW_CHANGE
argument_list|)
condition|)
block|{
return|return
name|NEW_CHANGE
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**    * Checks if a (magic branch)/branch_name reference exists in the destination repository and only    * returns Capable.OK if it does not match any.    *    *<p>These block the client from being able to even send us a pack file, as it is very unlikely    * the user passed the --force flag and the new commit is probably not going to fast-forward the    * branch.    */
DECL|method|checkMagicBranchRefs (Repository repo, Project project)
specifier|public
specifier|static
name|Capable
name|checkMagicBranchRefs
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|Project
name|project
parameter_list|)
block|{
return|return
name|checkMagicBranchRef
argument_list|(
name|NEW_CHANGE
argument_list|,
name|repo
argument_list|,
name|project
argument_list|)
return|;
block|}
DECL|method|checkMagicBranchRef (String branchName, Repository repo, Project project)
specifier|private
specifier|static
name|Capable
name|checkMagicBranchRef
parameter_list|(
name|String
name|branchName
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|Project
name|project
parameter_list|)
block|{
name|List
argument_list|<
name|Ref
argument_list|>
name|blockingFors
decl_stmt|;
try|try
block|{
name|blockingFors
operator|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefsByPrefix
argument_list|(
name|branchName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|String
name|projName
init|=
name|project
operator|.
name|getName
argument_list|()
decl_stmt|;
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|err
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot scan refs in '%s'"
argument_list|,
name|projName
argument_list|)
expr_stmt|;
return|return
operator|new
name|Capable
argument_list|(
literal|"Server process cannot read '"
operator|+
name|projName
operator|+
literal|"'"
argument_list|)
return|;
block|}
if|if
condition|(
operator|!
name|blockingFors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|projName
init|=
name|project
operator|.
name|getName
argument_list|()
decl_stmt|;
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Repository '%s' needs the following refs removed to receive changes: %s"
argument_list|,
name|projName
argument_list|,
name|blockingFors
argument_list|)
expr_stmt|;
return|return
operator|new
name|Capable
argument_list|(
literal|"One or more "
operator|+
name|branchName
operator|+
literal|" names blocks change upload"
argument_list|)
return|;
block|}
return|return
name|Capable
operator|.
name|OK
return|;
block|}
DECL|method|MagicBranch ()
specifier|private
name|MagicBranch
parameter_list|()
block|{}
block|}
end_class

end_unit

