begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.auth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|parboiled
operator|.
name|common
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An authenticated user as specified by the AuthBackend.  */
end_comment

begin_class
DECL|class|AuthUser
specifier|public
class|class
name|AuthUser
block|{
comment|/**    * Globally unique identifier for the user.    */
DECL|class|UUID
specifier|public
specifier|final
specifier|static
class|class
name|UUID
block|{
DECL|field|uuid
specifier|private
specifier|final
name|String
name|uuid
decl_stmt|;
comment|/**      * A new unique identifier.      *      * @param uuid the unique identifier.      */
DECL|method|UUID (String uuid)
specifier|public
name|UUID
parameter_list|(
name|String
name|uuid
parameter_list|)
block|{
name|this
operator|.
name|uuid
operator|=
name|checkNotNull
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
block|}
comment|/** @return the globally unique identifier. */
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|UUID
condition|)
block|{
return|return
name|get
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|UUID
operator|)
name|obj
operator|)
operator|.
name|get
argument_list|()
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
name|get
argument_list|()
operator|.
name|hashCode
argument_list|()
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
name|String
operator|.
name|format
argument_list|(
literal|"AuthUser.UUID[%s]"
argument_list|,
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|field|uuid
specifier|private
specifier|final
name|UUID
name|uuid
decl_stmt|;
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
comment|/**    * An authenticated user.    *    * @param uuid the globally unique ID.    * @param username the name of the authenticated user.    */
DECL|method|AuthUser (UUID uuid, @Nullable String username)
specifier|public
name|AuthUser
parameter_list|(
name|UUID
name|uuid
parameter_list|,
annotation|@
name|Nullable
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|uuid
operator|=
name|checkNotNull
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|/** @return the globally unique identifier. */
DECL|method|getUUID ()
specifier|public
specifier|final
name|UUID
name|getUUID
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
comment|/** @return the backend specific user name, or null if one does not exist. */
annotation|@
name|Nullable
DECL|method|getUsername ()
specifier|public
specifier|final
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/** @return {@code true} if {@link #getUsername()} is not null. */
DECL|method|hasUsername ()
specifier|public
specifier|final
name|boolean
name|hasUsername
parameter_list|()
block|{
return|return
name|getUsername
argument_list|()
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|AuthUser
condition|)
block|{
return|return
name|getUUID
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|AuthUser
operator|)
name|obj
operator|)
operator|.
name|getUUID
argument_list|()
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
name|getUUID
argument_list|()
operator|.
name|hashCode
argument_list|()
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
name|String
operator|.
name|format
argument_list|(
literal|"AuthUser[uuid=%s, username=%s]"
argument_list|,
name|getUUID
argument_list|()
argument_list|,
name|getUsername
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

