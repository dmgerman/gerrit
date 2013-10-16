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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|base
operator|.
name|Throwables
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterables
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|Account
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
name|account
operator|.
name|AccountDirectory
operator|.
name|DirectoryException
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
name|account
operator|.
name|AccountDirectory
operator|.
name|FillOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|AccountInfo
specifier|public
class|class
name|AccountInfo
block|{
DECL|class|Loader
specifier|public
specifier|static
class|class
name|Loader
block|{
DECL|field|DETAILED_OPTIONS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|DETAILED_OPTIONS
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|FillOptions
operator|.
name|NAME
argument_list|,
name|FillOptions
operator|.
name|EMAIL
argument_list|,
name|FillOptions
operator|.
name|USERNAME
argument_list|,
name|FillOptions
operator|.
name|AVATARS
argument_list|)
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (boolean detailed)
name|Loader
name|create
parameter_list|(
name|boolean
name|detailed
parameter_list|)
function_decl|;
block|}
DECL|field|directory
specifier|private
specifier|final
name|InternalAccountDirectory
name|directory
decl_stmt|;
DECL|field|detailed
specifier|private
specifier|final
name|boolean
name|detailed
decl_stmt|;
DECL|field|created
specifier|private
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountInfo
argument_list|>
name|created
decl_stmt|;
DECL|field|provided
specifier|private
specifier|final
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|provided
decl_stmt|;
annotation|@
name|Inject
DECL|method|Loader (InternalAccountDirectory directory, @Assisted boolean detailed)
name|Loader
parameter_list|(
name|InternalAccountDirectory
name|directory
parameter_list|,
annotation|@
name|Assisted
name|boolean
name|detailed
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|directory
expr_stmt|;
name|this
operator|.
name|detailed
operator|=
name|detailed
expr_stmt|;
name|created
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
name|provided
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
block|}
DECL|method|get (Account.Id id)
specifier|public
name|AccountInfo
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AccountInfo
name|info
init|=
name|created
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|info
operator|=
operator|new
name|AccountInfo
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|detailed
condition|)
block|{
name|info
operator|.
name|_account_id
operator|=
name|id
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|created
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
return|return
name|info
return|;
block|}
DECL|method|put (AccountInfo info)
specifier|public
name|void
name|put
parameter_list|(
name|AccountInfo
name|info
parameter_list|)
block|{
if|if
condition|(
name|detailed
condition|)
block|{
name|info
operator|.
name|_account_id
operator|=
name|info
operator|.
name|_id
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|provided
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
DECL|method|fill ()
specifier|public
name|void
name|fill
parameter_list|()
throws|throws
name|OrmException
block|{
try|try
block|{
name|directory
operator|.
name|fillAccountInfo
argument_list|(
name|Iterables
operator|.
name|concat
argument_list|(
name|created
operator|.
name|values
argument_list|()
argument_list|,
name|provided
argument_list|)
argument_list|,
name|detailed
condition|?
name|DETAILED_OPTIONS
else|:
name|EnumSet
operator|.
name|of
argument_list|(
name|FillOptions
operator|.
name|NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DirectoryException
name|e
parameter_list|)
block|{
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|OrmException
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|fill (Collection<? extends AccountInfo> infos)
specifier|public
name|void
name|fill
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|AccountInfo
argument_list|>
name|infos
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|AccountInfo
name|info
range|:
name|infos
control|)
block|{
name|put
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
name|fill
argument_list|()
expr_stmt|;
block|}
block|}
DECL|field|_id
specifier|public
specifier|transient
name|Account
operator|.
name|Id
name|_id
decl_stmt|;
DECL|method|AccountInfo (Account.Id id)
specifier|public
name|AccountInfo
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|_id
operator|=
name|id
expr_stmt|;
block|}
DECL|field|_account_id
specifier|public
name|Integer
name|_account_id
decl_stmt|;
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|email
specifier|public
name|String
name|email
decl_stmt|;
DECL|field|username
specifier|public
name|String
name|username
decl_stmt|;
DECL|field|avatars
specifier|public
name|List
argument_list|<
name|AvatarInfo
argument_list|>
name|avatars
decl_stmt|;
DECL|class|AvatarInfo
specifier|public
specifier|static
class|class
name|AvatarInfo
block|{
comment|/**      * Size in pixels the UI prefers an avatar image to be.      *      * The web UI prefers avatar images to be square, both      * the height and width of the image should be this size.      * The height is the more important dimension to match      * than the width.      */
DECL|field|DEFAULT_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SIZE
init|=
literal|26
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|height
specifier|public
name|Integer
name|height
decl_stmt|;
DECL|field|width
specifier|public
name|Integer
name|width
decl_stmt|;
block|}
block|}
end_class

end_unit

