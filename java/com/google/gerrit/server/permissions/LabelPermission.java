begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|LabelPermission
operator|.
name|ForUser
operator|.
name|ON_BEHALF_OF
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|LabelPermission
operator|.
name|ForUser
operator|.
name|SELF
import|;
end_import

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
name|data
operator|.
name|LabelType
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
name|LabelValue
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
name|util
operator|.
name|LabelVote
import|;
end_import

begin_comment
comment|/** Permission representing a label. */
end_comment

begin_class
DECL|class|LabelPermission
specifier|public
class|class
name|LabelPermission
implements|implements
name|ChangePermissionOrLabel
block|{
DECL|enum|ForUser
specifier|public
enum|enum
name|ForUser
block|{
DECL|enumConstant|SELF
name|SELF
block|,
DECL|enumConstant|ON_BEHALF_OF
name|ON_BEHALF_OF
block|;   }
DECL|field|forUser
specifier|private
specifier|final
name|ForUser
name|forUser
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
comment|/**    * Construct a reference to a label permission.    *    * @param type type description of the label.    */
DECL|method|LabelPermission (LabelType type)
specifier|public
name|LabelPermission
parameter_list|(
name|LabelType
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|SELF
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**    * Construct a reference to a label permission.    *    * @param forUser {@code SELF} (default) or {@code ON_BEHALF_OF} for labelAs behavior.    * @param type type description of the label.    */
DECL|method|LabelPermission (ForUser forUser, LabelType type)
specifier|public
name|LabelPermission
parameter_list|(
name|ForUser
name|forUser
parameter_list|,
name|LabelType
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|forUser
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Construct a reference to a label permission.    *    * @param name name of the label, e.g. {@code "Code-Review"} or {@code "Verified"}.    */
DECL|method|LabelPermission (String name)
specifier|public
name|LabelPermission
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|SELF
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**    * Construct a reference to a label permission.    *    * @param forUser {@code SELF} (default) or {@code ON_BEHALF_OF} for labelAs behavior.    * @param name name of the label, e.g. {@code "Code-Review"} or {@code "Verified"}.    */
DECL|method|LabelPermission (ForUser forUser, String name)
specifier|public
name|LabelPermission
parameter_list|(
name|ForUser
name|forUser
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|forUser
operator|=
name|requireNonNull
argument_list|(
name|forUser
argument_list|,
literal|"ForUser"
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|LabelType
operator|.
name|checkName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/** @return {@code SELF} or {@code ON_BEHALF_OF} (or labelAs). */
DECL|method|forUser ()
specifier|public
name|ForUser
name|forUser
parameter_list|()
block|{
return|return
name|forUser
return|;
block|}
comment|/** @return name of the label, e.g. {@code "Code-Review"}. */
DECL|method|label ()
specifier|public
name|String
name|label
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
if|if
condition|(
name|forUser
operator|==
name|ON_BEHALF_OF
condition|)
block|{
return|return
literal|"label on behalf of "
operator|+
name|name
return|;
block|}
return|return
literal|"label "
operator|+
name|name
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
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|LabelPermission
condition|)
block|{
name|LabelPermission
name|b
init|=
operator|(
name|LabelPermission
operator|)
name|other
decl_stmt|;
return|return
name|forUser
operator|==
name|b
operator|.
name|forUser
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|b
operator|.
name|name
argument_list|)
return|;
block|}
return|return
literal|false
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
if|if
condition|(
name|forUser
operator|==
name|ON_BEHALF_OF
condition|)
block|{
return|return
literal|"LabelAs["
operator|+
name|name
operator|+
literal|']'
return|;
block|}
return|return
literal|"Label["
operator|+
name|name
operator|+
literal|']'
return|;
block|}
comment|/** A {@link LabelPermission} at a specific value. */
DECL|class|WithValue
specifier|public
specifier|static
class|class
name|WithValue
implements|implements
name|ChangePermissionOrLabel
block|{
DECL|field|forUser
specifier|private
specifier|final
name|ForUser
name|forUser
decl_stmt|;
DECL|field|label
specifier|private
specifier|final
name|LabelVote
name|label
decl_stmt|;
comment|/**      * Construct a reference to a label at a specific value.      *      * @param type description of the label.      * @param value numeric score assigned to the label.      */
DECL|method|WithValue (LabelType type, LabelValue value)
specifier|public
name|WithValue
parameter_list|(
name|LabelType
name|type
parameter_list|,
name|LabelValue
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|SELF
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param type description of the label.      * @param value numeric score assigned to the label.      */
DECL|method|WithValue (LabelType type, short value)
specifier|public
name|WithValue
parameter_list|(
name|LabelType
name|type
parameter_list|,
name|short
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|SELF
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param forUser {@code SELF} (default) or {@code ON_BEHALF_OF} for labelAs behavior.      * @param type description of the label.      * @param value numeric score assigned to the label.      */
DECL|method|WithValue (ForUser forUser, LabelType type, LabelValue value)
specifier|public
name|WithValue
parameter_list|(
name|ForUser
name|forUser
parameter_list|,
name|LabelType
name|type
parameter_list|,
name|LabelValue
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|forUser
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|value
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param forUser {@code SELF} (default) or {@code ON_BEHALF_OF} for labelAs behavior.      * @param type description of the label.      * @param value numeric score assigned to the label.      */
DECL|method|WithValue (ForUser forUser, LabelType type, short value)
specifier|public
name|WithValue
parameter_list|(
name|ForUser
name|forUser
parameter_list|,
name|LabelType
name|type
parameter_list|,
name|short
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|forUser
argument_list|,
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param name name of the label, e.g. {@code "Code-Review"} or {@code "Verified"}.      * @param value numeric score assigned to the label.      */
DECL|method|WithValue (String name, short value)
specifier|public
name|WithValue
parameter_list|(
name|String
name|name
parameter_list|,
name|short
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|SELF
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param forUser {@code SELF} (default) or {@code ON_BEHALF_OF} for labelAs behavior.      * @param name name of the label, e.g. {@code "Code-Review"} or {@code "Verified"}.      * @param value numeric score assigned to the label.      */
DECL|method|WithValue (ForUser forUser, String name, short value)
specifier|public
name|WithValue
parameter_list|(
name|ForUser
name|forUser
parameter_list|,
name|String
name|name
parameter_list|,
name|short
name|value
parameter_list|)
block|{
name|this
argument_list|(
name|forUser
argument_list|,
name|LabelVote
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param label label name and vote.      */
DECL|method|WithValue (LabelVote label)
specifier|public
name|WithValue
parameter_list|(
name|LabelVote
name|label
parameter_list|)
block|{
name|this
argument_list|(
name|SELF
argument_list|,
name|label
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a reference to a label at a specific value.      *      * @param forUser {@code SELF} (default) or {@code ON_BEHALF_OF} for labelAs behavior.      * @param label label name and vote.      */
DECL|method|WithValue (ForUser forUser, LabelVote label)
specifier|public
name|WithValue
parameter_list|(
name|ForUser
name|forUser
parameter_list|,
name|LabelVote
name|label
parameter_list|)
block|{
name|this
operator|.
name|forUser
operator|=
name|requireNonNull
argument_list|(
name|forUser
argument_list|,
literal|"ForUser"
argument_list|)
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|requireNonNull
argument_list|(
name|label
argument_list|,
literal|"LabelVote"
argument_list|)
expr_stmt|;
block|}
comment|/** @return {@code SELF} or {@code ON_BEHALF_OF} (or labelAs). */
DECL|method|forUser ()
specifier|public
name|ForUser
name|forUser
parameter_list|()
block|{
return|return
name|forUser
return|;
block|}
comment|/** @return name of the label, e.g. {@code "Code-Review"}. */
DECL|method|label ()
specifier|public
name|String
name|label
parameter_list|()
block|{
return|return
name|label
operator|.
name|label
argument_list|()
return|;
block|}
comment|/** @return specific value of the label, e.g. 1 or 2. */
DECL|method|value ()
specifier|public
name|short
name|value
parameter_list|()
block|{
return|return
name|label
operator|.
name|value
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
if|if
condition|(
name|forUser
operator|==
name|ON_BEHALF_OF
condition|)
block|{
return|return
literal|"label on behalf of "
operator|+
name|label
operator|.
name|formatWithEquals
argument_list|()
return|;
block|}
return|return
literal|"label "
operator|+
name|label
operator|.
name|formatWithEquals
argument_list|()
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
name|label
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|WithValue
condition|)
block|{
name|WithValue
name|b
init|=
operator|(
name|WithValue
operator|)
name|other
decl_stmt|;
return|return
name|forUser
operator|==
name|b
operator|.
name|forUser
operator|&&
name|label
operator|.
name|equals
argument_list|(
name|b
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|forUser
operator|==
name|ON_BEHALF_OF
condition|)
block|{
return|return
literal|"LabelAs["
operator|+
name|label
operator|.
name|format
argument_list|()
operator|+
literal|']'
return|;
block|}
return|return
literal|"Label["
operator|+
name|label
operator|.
name|format
argument_list|()
operator|+
literal|']'
return|;
block|}
block|}
block|}
end_class

end_unit

