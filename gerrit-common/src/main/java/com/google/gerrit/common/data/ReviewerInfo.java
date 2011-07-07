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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_comment
comment|/**  * Suggested reviewer for a change. Can be a user ({@link AccountInfo}) or a  * group ({@link GroupReference}).  */
end_comment

begin_class
DECL|class|ReviewerInfo
specifier|public
class|class
name|ReviewerInfo
implements|implements
name|Comparable
argument_list|<
name|ReviewerInfo
argument_list|>
block|{
DECL|field|accountInfo
specifier|private
name|AccountInfo
name|accountInfo
decl_stmt|;
DECL|field|groupReference
specifier|private
name|GroupReference
name|groupReference
decl_stmt|;
DECL|method|ReviewerInfo ()
specifier|protected
name|ReviewerInfo
parameter_list|()
block|{   }
DECL|method|ReviewerInfo (final AccountInfo accountInfo)
specifier|public
name|ReviewerInfo
parameter_list|(
specifier|final
name|AccountInfo
name|accountInfo
parameter_list|)
block|{
name|this
operator|.
name|accountInfo
operator|=
name|accountInfo
expr_stmt|;
block|}
DECL|method|ReviewerInfo (final GroupReference groupReference)
specifier|public
name|ReviewerInfo
parameter_list|(
specifier|final
name|GroupReference
name|groupReference
parameter_list|)
block|{
name|this
operator|.
name|groupReference
operator|=
name|groupReference
expr_stmt|;
block|}
DECL|method|getAccountInfo ()
specifier|public
name|AccountInfo
name|getAccountInfo
parameter_list|()
block|{
return|return
name|accountInfo
return|;
block|}
DECL|method|getGroup ()
specifier|public
name|GroupReference
name|getGroup
parameter_list|()
block|{
return|return
name|groupReference
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (final ReviewerInfo o)
specifier|public
name|int
name|compareTo
parameter_list|(
specifier|final
name|ReviewerInfo
name|o
parameter_list|)
block|{
return|return
name|getSortValue
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o
operator|.
name|getSortValue
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSortValue ()
specifier|private
name|String
name|getSortValue
parameter_list|()
block|{
if|if
condition|(
name|accountInfo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|accountInfo
operator|.
name|getPreferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|accountInfo
operator|.
name|getPreferredEmail
argument_list|()
return|;
block|}
return|return
name|accountInfo
operator|.
name|getFullName
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|groupReference
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

