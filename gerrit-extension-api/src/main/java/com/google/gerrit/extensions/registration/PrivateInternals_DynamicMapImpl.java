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
DECL|package|com.google.gerrit.extensions.registration
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
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
name|annotations
operator|.
name|Export
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
name|Key
import|;
end_import

begin_comment
comment|/**<b>DO NOT USE</b> */
end_comment

begin_class
DECL|class|PrivateInternals_DynamicMapImpl
specifier|public
class|class
name|PrivateInternals_DynamicMapImpl
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DynamicMap
argument_list|<
name|T
argument_list|>
block|{
DECL|method|PrivateInternals_DynamicMapImpl ()
name|PrivateInternals_DynamicMapImpl
parameter_list|()
block|{   }
comment|/**    * Store one new element into the map.    *    * @param pluginName unique name of the plugin providing the export.    * @param exportName name the plugin has exported the item as.    * @param item the item to add to the collection. Must not be null.    * @return handle to remove the item at a later point in time.    */
DECL|method|put ( String pluginName, String exportName, final T item)
specifier|public
name|RegistrationHandle
name|put
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|exportName
parameter_list|,
specifier|final
name|T
name|item
parameter_list|)
block|{
specifier|final
name|NamePair
name|key
init|=
operator|new
name|NamePair
argument_list|(
name|pluginName
argument_list|,
name|exportName
argument_list|)
decl_stmt|;
name|items
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|item
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegistrationHandle
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|items
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|item
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**    * Store one new element that may be hot-replaceable in the future.    *    * @param pluginName unique name of the plugin providing the export.    * @param key unique description from the item's Guice binding. This can be    *        later obtained from the registration handle to facilitate matching    *        with the new equivalent instance during a hot reload. The key must    *        use an {@link @Export} annotation.    * @param item the item to add to the collection right now. Must not be null.    * @return a handle that can remove this item later, or hot-swap the item    *         without it ever leaving the collection.    */
DECL|method|put ( String pluginName, Key<T> key, T item)
specifier|public
name|ReloadableRegistrationHandle
argument_list|<
name|T
argument_list|>
name|put
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|T
name|item
parameter_list|)
block|{
name|String
name|exportName
init|=
operator|(
operator|(
name|Export
operator|)
name|key
operator|.
name|getAnnotation
argument_list|()
operator|)
operator|.
name|value
argument_list|()
decl_stmt|;
name|NamePair
name|np
init|=
operator|new
name|NamePair
argument_list|(
name|pluginName
argument_list|,
name|exportName
argument_list|)
decl_stmt|;
name|items
operator|.
name|put
argument_list|(
name|np
argument_list|,
name|item
argument_list|)
expr_stmt|;
return|return
operator|new
name|ReloadableHandle
argument_list|(
name|np
argument_list|,
name|key
argument_list|,
name|item
argument_list|)
return|;
block|}
DECL|class|ReloadableHandle
specifier|private
class|class
name|ReloadableHandle
implements|implements
name|ReloadableRegistrationHandle
argument_list|<
name|T
argument_list|>
block|{
DECL|field|np
specifier|private
specifier|final
name|NamePair
name|np
decl_stmt|;
DECL|field|key
specifier|private
specifier|final
name|Key
argument_list|<
name|T
argument_list|>
name|key
decl_stmt|;
DECL|field|item
specifier|private
specifier|final
name|T
name|item
decl_stmt|;
DECL|method|ReloadableHandle (NamePair np, Key<T> key, T item)
name|ReloadableHandle
parameter_list|(
name|NamePair
name|np
parameter_list|,
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|T
name|item
parameter_list|)
block|{
name|this
operator|.
name|np
operator|=
name|np
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|item
operator|=
name|item
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|items
operator|.
name|remove
argument_list|(
name|np
argument_list|,
name|item
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getKey ()
specifier|public
name|Key
argument_list|<
name|T
argument_list|>
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
annotation|@
name|Override
DECL|method|replace (Key<T> newKey, T newItem)
specifier|public
name|ReloadableHandle
name|replace
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|newKey
parameter_list|,
name|T
name|newItem
parameter_list|)
block|{
if|if
condition|(
name|items
operator|.
name|replace
argument_list|(
name|np
argument_list|,
name|item
argument_list|,
name|newItem
argument_list|)
condition|)
block|{
return|return
operator|new
name|ReloadableHandle
argument_list|(
name|np
argument_list|,
name|newKey
argument_list|,
name|newItem
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

