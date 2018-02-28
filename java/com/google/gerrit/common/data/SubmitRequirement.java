begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Describes a requirement to submit a change. */
end_comment

begin_class
DECL|class|SubmitRequirement
specifier|public
specifier|final
class|class
name|SubmitRequirement
block|{
DECL|field|shortReason
specifier|private
specifier|final
name|String
name|shortReason
decl_stmt|;
DECL|field|fullReason
specifier|private
specifier|final
name|String
name|fullReason
decl_stmt|;
DECL|field|label
annotation|@
name|Nullable
specifier|private
specifier|final
name|String
name|label
decl_stmt|;
DECL|method|SubmitRequirement (String shortReason, String fullReason, @Nullable String label)
specifier|public
name|SubmitRequirement
parameter_list|(
name|String
name|shortReason
parameter_list|,
name|String
name|fullReason
parameter_list|,
annotation|@
name|Nullable
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|shortReason
operator|=
name|requireNonNull
argument_list|(
name|shortReason
argument_list|)
expr_stmt|;
name|this
operator|.
name|fullReason
operator|=
name|requireNonNull
argument_list|(
name|fullReason
argument_list|)
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|shortReason ()
specifier|public
name|String
name|shortReason
parameter_list|()
block|{
return|return
name|shortReason
return|;
block|}
DECL|method|fullReason ()
specifier|public
name|String
name|fullReason
parameter_list|()
block|{
return|return
name|fullReason
return|;
block|}
DECL|method|label ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|label
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|label
argument_list|)
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
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|SubmitRequirement
condition|)
block|{
name|SubmitRequirement
name|that
init|=
operator|(
name|SubmitRequirement
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|shortReason
argument_list|,
name|that
operator|.
name|shortReason
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|fullReason
argument_list|,
name|that
operator|.
name|fullReason
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|label
argument_list|,
name|that
operator|.
name|label
argument_list|)
return|;
block|}
return|return
literal|false
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
name|shortReason
argument_list|,
name|fullReason
argument_list|,
name|label
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SubmitRequirement{"
operator|+
literal|"shortReason='"
operator|+
name|shortReason
operator|+
literal|'\''
operator|+
literal|", fullReason='"
operator|+
name|fullReason
operator|+
literal|'\''
operator|+
literal|", label='"
operator|+
name|label
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

