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
name|RevCommit
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|GitUtil
specifier|public
class|class
name|GitUtil
block|{
comment|/**    * @param git    * @param commitId    * @param parentNum    * @return the {@code paretNo} parent of given commit or {@code null}    *             when {@code parentNo} exceed number of {@code commitId} parents.    * @throws IncorrectObjectTypeException    *             the supplied id is not a commit or an annotated tag.    * @throws IOException    *             a pack file or loose object could not be read.    */
DECL|method|getParent (Repository git, ObjectId commitId, int parentNum)
specifier|public
specifier|static
name|RevCommit
name|getParent
parameter_list|(
name|Repository
name|git
parameter_list|,
name|ObjectId
name|commitId
parameter_list|,
name|int
name|parentNum
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|RevWalk
name|walk
init|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|RevCommit
name|commit
init|=
name|walk
operator|.
name|parseCommit
argument_list|(
name|commitId
argument_list|)
decl_stmt|;
if|if
condition|(
name|commit
operator|.
name|getParentCount
argument_list|()
operator|>
name|parentNum
condition|)
block|{
return|return
name|commit
operator|.
name|getParent
argument_list|(
name|parentNum
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|GitUtil ()
specifier|private
name|GitUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

