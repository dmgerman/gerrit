begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gwtexpui.globalkey.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyPressEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyPressHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_class
DECL|class|KeyCommandSet
specifier|public
class|class
name|KeyCommandSet
implements|implements
name|KeyPressHandler
block|{
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|KeyCommand
argument_list|>
name|map
decl_stmt|;
DECL|field|sets
specifier|private
name|List
argument_list|<
name|KeyCommandSet
argument_list|>
name|sets
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|KeyCommandSet ()
specifier|public
name|KeyCommandSet
parameter_list|()
block|{
name|this
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|KeyCommandSet (final String setName)
specifier|public
name|KeyCommandSet
parameter_list|(
specifier|final
name|String
name|setName
parameter_list|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|KeyCommand
argument_list|>
argument_list|()
expr_stmt|;
name|name
operator|=
name|setName
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (final String setName)
specifier|public
name|void
name|setName
parameter_list|(
specifier|final
name|String
name|setName
parameter_list|)
block|{
assert|assert
name|setName
operator|!=
literal|null
assert|;
name|name
operator|=
name|setName
expr_stmt|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|map
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|add (final KeyCommand k)
specifier|public
name|void
name|add
parameter_list|(
specifier|final
name|KeyCommand
name|k
parameter_list|)
block|{
assert|assert
operator|!
name|map
operator|.
name|containsKey
argument_list|(
name|k
operator|.
name|keyMask
argument_list|)
operator|:
literal|"Key "
operator|+
name|k
operator|.
name|describeKeyStroke
argument_list|()
operator|.
name|asString
argument_list|()
operator|+
literal|" already registered"
assert|;
if|if
condition|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
name|k
operator|.
name|keyMask
argument_list|)
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|k
operator|.
name|keyMask
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|remove (final KeyCommand k)
specifier|public
name|void
name|remove
parameter_list|(
specifier|final
name|KeyCommand
name|k
parameter_list|)
block|{
assert|assert
name|map
operator|.
name|get
argument_list|(
name|k
operator|.
name|keyMask
argument_list|)
operator|==
name|k
assert|;
name|map
operator|.
name|remove
argument_list|(
name|k
operator|.
name|keyMask
argument_list|)
expr_stmt|;
block|}
DECL|method|add (final KeyCommandSet set)
specifier|public
name|void
name|add
parameter_list|(
specifier|final
name|KeyCommandSet
name|set
parameter_list|)
block|{
if|if
condition|(
name|sets
operator|==
literal|null
condition|)
block|{
name|sets
operator|=
operator|new
name|ArrayList
argument_list|<
name|KeyCommandSet
argument_list|>
argument_list|()
expr_stmt|;
block|}
assert|assert
operator|!
name|sets
operator|.
name|contains
argument_list|(
name|set
argument_list|)
assert|;
name|sets
operator|.
name|add
argument_list|(
name|set
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|KeyCommand
name|k
range|:
name|set
operator|.
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|remove (final KeyCommandSet set)
specifier|public
name|void
name|remove
parameter_list|(
specifier|final
name|KeyCommandSet
name|set
parameter_list|)
block|{
assert|assert
name|sets
operator|!=
literal|null
assert|;
assert|assert
name|sets
operator|.
name|contains
argument_list|(
name|set
argument_list|)
assert|;
name|sets
operator|.
name|remove
argument_list|(
name|set
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|KeyCommand
name|k
range|:
name|set
operator|.
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|remove
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|filter (final KeyCommandFilter filter)
specifier|public
name|void
name|filter
parameter_list|(
specifier|final
name|KeyCommandFilter
name|filter
parameter_list|)
block|{
if|if
condition|(
name|sets
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|KeyCommandSet
name|s
range|:
name|sets
control|)
block|{
name|s
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|KeyCommand
argument_list|>
name|i
init|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|KeyCommand
name|kc
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|filter
operator|.
name|include
argument_list|(
name|kc
argument_list|)
condition|)
block|{
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|kc
operator|instanceof
name|CompoundKeyCommand
condition|)
block|{
operator|(
operator|(
name|CompoundKeyCommand
operator|)
name|kc
operator|)
operator|.
name|set
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getKeys ()
specifier|public
name|Collection
argument_list|<
name|KeyCommand
argument_list|>
name|getKeys
parameter_list|()
block|{
return|return
name|map
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|getSets ()
specifier|public
name|Collection
argument_list|<
name|KeyCommandSet
argument_list|>
name|getSets
parameter_list|()
block|{
return|return
name|sets
operator|!=
literal|null
condition|?
name|sets
else|:
name|Collections
operator|.
expr|<
name|KeyCommandSet
operator|>
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent event)
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
specifier|final
name|KeyCommand
name|k
init|=
name|map
operator|.
name|get
argument_list|(
name|toMask
argument_list|(
name|event
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
condition|)
block|{
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|event
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
name|k
operator|.
name|onKeyPress
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|toMask (final KeyPressEvent event)
specifier|static
name|int
name|toMask
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|int
name|mask
init|=
name|event
operator|.
name|getCharCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|event
operator|.
name|isAltKeyDown
argument_list|()
condition|)
block|{
name|mask
operator||=
name|KeyCommand
operator|.
name|M_ALT
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|.
name|isControlKeyDown
argument_list|()
condition|)
block|{
name|mask
operator||=
name|KeyCommand
operator|.
name|M_CTRL
expr_stmt|;
block|}
if|if
condition|(
name|event
operator|.
name|isMetaKeyDown
argument_list|()
condition|)
block|{
name|mask
operator||=
name|KeyCommand
operator|.
name|M_META
expr_stmt|;
block|}
return|return
name|mask
return|;
block|}
block|}
end_class

end_unit

