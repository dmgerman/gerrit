begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|AnyObjectId
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

begin_comment
comment|/** Additional data about a {@link PatchSet} not normally loaded. */
end_comment

begin_class
DECL|class|PatchSetInfo
specifier|public
specifier|final
class|class
name|PatchSetInfo
block|{
DECL|class|ParentInfo
specifier|public
specifier|static
class|class
name|ParentInfo
block|{
DECL|field|commitId
specifier|public
name|ObjectId
name|commitId
decl_stmt|;
DECL|field|shortMessage
specifier|public
name|String
name|shortMessage
decl_stmt|;
DECL|method|ParentInfo (AnyObjectId commitId, String shortMessage)
specifier|public
name|ParentInfo
parameter_list|(
name|AnyObjectId
name|commitId
parameter_list|,
name|String
name|shortMessage
parameter_list|)
block|{
name|this
operator|.
name|commitId
operator|=
name|commitId
operator|.
name|copy
argument_list|()
expr_stmt|;
name|this
operator|.
name|shortMessage
operator|=
name|requireNonNull
argument_list|(
name|shortMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|ParentInfo ()
specifier|protected
name|ParentInfo
parameter_list|()
block|{}
block|}
DECL|field|key
specifier|protected
name|PatchSet
operator|.
name|Id
name|key
decl_stmt|;
comment|/** First line of {@link #message}. */
DECL|field|subject
specifier|protected
name|String
name|subject
decl_stmt|;
comment|/** The complete description of the change the patch set introduces. */
DECL|field|message
specifier|protected
name|String
name|message
decl_stmt|;
comment|/** Identity of who wrote the patch set. May differ from {@link #committer}. */
DECL|field|author
specifier|protected
name|UserIdentity
name|author
decl_stmt|;
comment|/** Identity of who committed the patch set to the VCS. */
DECL|field|committer
specifier|protected
name|UserIdentity
name|committer
decl_stmt|;
comment|/** List of parents of the patch set. */
DECL|field|parents
specifier|protected
name|List
argument_list|<
name|ParentInfo
argument_list|>
name|parents
decl_stmt|;
comment|/** ID of commit. */
DECL|field|commitId
specifier|protected
name|ObjectId
name|commitId
decl_stmt|;
comment|/** Optional user-supplied description for the patch set. */
DECL|field|description
specifier|protected
name|String
name|description
decl_stmt|;
DECL|method|PatchSetInfo ()
specifier|protected
name|PatchSetInfo
parameter_list|()
block|{}
DECL|method|PatchSetInfo (PatchSet.Id k)
specifier|public
name|PatchSetInfo
parameter_list|(
name|PatchSet
operator|.
name|Id
name|k
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|setSubject (String s)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|length
argument_list|()
operator|>
literal|255
condition|)
block|{
name|subject
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|255
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|subject
operator|=
name|s
expr_stmt|;
block|}
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|setMessage (String m)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|m
parameter_list|)
block|{
name|message
operator|=
name|m
expr_stmt|;
block|}
DECL|method|getAuthor ()
specifier|public
name|UserIdentity
name|getAuthor
parameter_list|()
block|{
return|return
name|author
return|;
block|}
DECL|method|setAuthor (UserIdentity u)
specifier|public
name|void
name|setAuthor
parameter_list|(
name|UserIdentity
name|u
parameter_list|)
block|{
name|author
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getCommitter ()
specifier|public
name|UserIdentity
name|getCommitter
parameter_list|()
block|{
return|return
name|committer
return|;
block|}
DECL|method|setCommitter (UserIdentity u)
specifier|public
name|void
name|setCommitter
parameter_list|(
name|UserIdentity
name|u
parameter_list|)
block|{
name|committer
operator|=
name|u
expr_stmt|;
block|}
DECL|method|setParents (List<ParentInfo> p)
specifier|public
name|void
name|setParents
parameter_list|(
name|List
argument_list|<
name|ParentInfo
argument_list|>
name|p
parameter_list|)
block|{
name|parents
operator|=
name|p
expr_stmt|;
block|}
DECL|method|getParents ()
specifier|public
name|List
argument_list|<
name|ParentInfo
argument_list|>
name|getParents
parameter_list|()
block|{
return|return
name|parents
return|;
block|}
DECL|method|setCommitId (AnyObjectId commitId)
specifier|public
name|void
name|setCommitId
parameter_list|(
name|AnyObjectId
name|commitId
parameter_list|)
block|{
name|this
operator|.
name|commitId
operator|=
name|commitId
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
DECL|method|getCommitId ()
specifier|public
name|ObjectId
name|getCommitId
parameter_list|()
block|{
return|return
name|commitId
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
end_class

end_unit

