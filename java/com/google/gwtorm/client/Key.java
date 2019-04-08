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
DECL|package|com.google.gwtorm.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
package|;
end_package

begin_comment
comment|/**  * Generic type for an entity key.  *  *<p>Although not required, entities should make their primary key type implement this interface,  * permitting traversal up through the containment hierarchy of the entity keys.  *  * @param<P> type of the parent key. If no parent, use {@link Key} itself.  */
end_comment

begin_interface
DECL|interface|Key
specifier|public
interface|interface
name|Key
parameter_list|<
name|P
extends|extends
name|Key
parameter_list|<
name|?
parameter_list|>
parameter_list|>
block|{
comment|/**    * Get the parent key instance.    *    * @return the parent key; null if this entity key is a root-level key.    */
DECL|method|getParentKey ()
specifier|public
name|P
name|getParentKey
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
function_decl|;
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
function_decl|;
comment|/** @return the key, encoded in a string format . */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
function_decl|;
comment|/** Reset this key instance to represent the data in the supplied string. */
DECL|method|fromString (String in)
specifier|public
name|void
name|fromString
parameter_list|(
name|String
name|in
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

