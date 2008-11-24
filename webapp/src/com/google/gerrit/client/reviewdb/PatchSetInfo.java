begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Column
import|;
end_import

begin_comment
comment|/**  * Additional data about a {@link PatchSet} not normally loaded.  *<p>  * This is stored out of band from the PatchSet itself, to reduce the size of  * each PatchSet record.  */
end_comment

begin_class
DECL|class|PatchSetInfo
specifier|public
specifier|final
class|class
name|PatchSetInfo
block|{
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|PatchSet
operator|.
name|Id
name|key
decl_stmt|;
comment|/** First line of {@link #message}. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|subject
specifier|protected
name|String
name|subject
decl_stmt|;
comment|/** The complete description of the change the patch set introduces. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|,
name|length
operator|=
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
DECL|field|message
specifier|protected
name|String
name|message
decl_stmt|;
comment|/** Identity of who wrote the patch set. May differ from {@link #committer}. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|author
specifier|protected
name|UserIdentity
name|author
decl_stmt|;
comment|/** Identity of who committed the patch set to the VCS. */
annotation|@
name|Column
argument_list|(
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|committer
specifier|protected
name|UserIdentity
name|committer
decl_stmt|;
DECL|method|PatchSetInfo ()
specifier|protected
name|PatchSetInfo
parameter_list|()
block|{   }
DECL|method|PatchSetInfo (final PatchSet.Id k)
specifier|public
name|PatchSetInfo
parameter_list|(
specifier|final
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
DECL|method|setSubject (final String s)
specifier|public
name|void
name|setSubject
parameter_list|(
specifier|final
name|String
name|s
parameter_list|)
block|{
name|subject
operator|=
name|s
expr_stmt|;
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
DECL|method|setMessage (final String m)
specifier|public
name|void
name|setMessage
parameter_list|(
specifier|final
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
DECL|method|setAuthor (final UserIdentity u)
specifier|public
name|void
name|setAuthor
parameter_list|(
specifier|final
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
DECL|method|setCommitter (final UserIdentity u)
specifier|public
name|void
name|setCommitter
parameter_list|(
specifier|final
name|UserIdentity
name|u
parameter_list|)
block|{
name|committer
operator|=
name|u
expr_stmt|;
block|}
block|}
end_class

end_unit

