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
comment|/** A revision identifier for a file or a change. */
end_comment

begin_class
DECL|class|RevId
specifier|public
specifier|final
class|class
name|RevId
block|{
DECL|field|LEN
specifier|public
specifier|static
specifier|final
name|int
name|LEN
init|=
literal|40
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|length
operator|=
name|LEN
argument_list|)
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|method|RevId ()
specifier|protected
name|RevId
parameter_list|()
block|{   }
DECL|method|RevId (final String str)
specifier|public
name|RevId
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
name|id
operator|=
name|str
expr_stmt|;
block|}
comment|/** @return the value of this revision id. */
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/** @return true if this revision id has all required digits. */
DECL|method|isComplete ()
specifier|public
name|boolean
name|isComplete
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|length
argument_list|()
operator|==
name|LEN
return|;
block|}
comment|/**    * @return if {@link #isComplete()},<code>this</code>; otherwise a new RevId    *         with 'z' appended on the end.    */
DECL|method|max ()
specifier|public
name|RevId
name|max
parameter_list|()
block|{
if|if
condition|(
name|isComplete
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
specifier|final
name|StringBuilder
name|revEnd
init|=
operator|new
name|StringBuilder
argument_list|(
name|get
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|revEnd
operator|.
name|append
argument_list|(
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|revEnd
operator|.
name|append
argument_list|(
literal|'z'
argument_list|)
expr_stmt|;
return|return
operator|new
name|RevId
argument_list|(
name|revEnd
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

