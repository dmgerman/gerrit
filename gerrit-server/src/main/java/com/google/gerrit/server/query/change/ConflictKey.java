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
name|extensions
operator|.
name|client
operator|.
name|SubmitType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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

begin_class
DECL|class|ConflictKey
specifier|public
class|class
name|ConflictKey
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2L
decl_stmt|;
DECL|field|commit
specifier|private
specifier|final
name|ObjectId
name|commit
decl_stmt|;
DECL|field|otherCommit
specifier|private
specifier|final
name|ObjectId
name|otherCommit
decl_stmt|;
DECL|field|submitType
specifier|private
specifier|final
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|contentMerge
specifier|private
specifier|final
name|boolean
name|contentMerge
decl_stmt|;
DECL|method|ConflictKey ( ObjectId commit, ObjectId otherCommit, SubmitType submitType, boolean contentMerge)
specifier|public
name|ConflictKey
parameter_list|(
name|ObjectId
name|commit
parameter_list|,
name|ObjectId
name|otherCommit
parameter_list|,
name|SubmitType
name|submitType
parameter_list|,
name|boolean
name|contentMerge
parameter_list|)
block|{
if|if
condition|(
name|SubmitType
operator|.
name|FAST_FORWARD_ONLY
operator|.
name|equals
argument_list|(
name|submitType
argument_list|)
operator|||
name|commit
operator|.
name|compareTo
argument_list|(
name|otherCommit
argument_list|)
operator|<
literal|0
condition|)
block|{
name|this
operator|.
name|commit
operator|=
name|commit
expr_stmt|;
name|this
operator|.
name|otherCommit
operator|=
name|otherCommit
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|commit
operator|=
name|otherCommit
expr_stmt|;
name|this
operator|.
name|otherCommit
operator|=
name|commit
expr_stmt|;
block|}
name|this
operator|.
name|submitType
operator|=
name|submitType
expr_stmt|;
name|this
operator|.
name|contentMerge
operator|=
name|contentMerge
expr_stmt|;
block|}
DECL|method|getCommit ()
specifier|public
name|ObjectId
name|getCommit
parameter_list|()
block|{
return|return
name|commit
return|;
block|}
DECL|method|getOtherCommit ()
specifier|public
name|ObjectId
name|getOtherCommit
parameter_list|()
block|{
return|return
name|otherCommit
return|;
block|}
DECL|method|getSubmitType ()
specifier|public
name|SubmitType
name|getSubmitType
parameter_list|()
block|{
return|return
name|submitType
return|;
block|}
DECL|method|isContentMerge ()
specifier|public
name|boolean
name|isContentMerge
parameter_list|()
block|{
return|return
name|contentMerge
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|ConflictKey
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ConflictKey
name|other
init|=
operator|(
name|ConflictKey
operator|)
name|o
decl_stmt|;
return|return
name|commit
operator|.
name|equals
argument_list|(
name|other
operator|.
name|commit
argument_list|)
operator|&&
name|otherCommit
operator|.
name|equals
argument_list|(
name|other
operator|.
name|otherCommit
argument_list|)
operator|&&
name|submitType
operator|.
name|equals
argument_list|(
name|other
operator|.
name|submitType
argument_list|)
operator|&&
name|contentMerge
operator|==
name|other
operator|.
name|contentMerge
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|commit
argument_list|,
name|otherCommit
argument_list|,
name|submitType
argument_list|,
name|contentMerge
argument_list|)
return|;
block|}
block|}
end_class

end_unit

