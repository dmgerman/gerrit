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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|AccountGroup
import|;
end_import

begin_comment
comment|/** Summary information about an {@link AccountGroup}, for simple tabular displays. */
end_comment

begin_class
DECL|class|GroupInfo
specifier|public
class|class
name|GroupInfo
block|{
DECL|field|uuid
specifier|protected
name|AccountGroup
operator|.
name|UUID
name|uuid
decl_stmt|;
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
DECL|field|description
specifier|protected
name|String
name|description
decl_stmt|;
DECL|field|url
specifier|protected
name|String
name|url
decl_stmt|;
DECL|method|GroupInfo ()
specifier|protected
name|GroupInfo
parameter_list|()
block|{}
comment|/**    * Create an anonymous group info, when only the id is known.    *    *<p>This constructor should only be a last-ditch effort, when the usual group lookup has failed    * and a stale group id has been discovered in the data store.    */
DECL|method|GroupInfo (AccountGroup.UUID uuid)
specifier|public
name|GroupInfo
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|this
operator|.
name|uuid
operator|=
name|uuid
expr_stmt|;
block|}
comment|/**    * Create a group description from a real data store record.    *    * @param a the data store record holding the specific group details.    */
DECL|method|GroupInfo (GroupDescription.Basic a)
specifier|public
name|GroupInfo
parameter_list|(
name|GroupDescription
operator|.
name|Basic
name|a
parameter_list|)
block|{
name|uuid
operator|=
name|a
operator|.
name|getGroupUUID
argument_list|()
expr_stmt|;
name|name
operator|=
name|a
operator|.
name|getName
argument_list|()
expr_stmt|;
name|url
operator|=
name|a
operator|.
name|getUrl
argument_list|()
expr_stmt|;
if|if
condition|(
name|a
operator|instanceof
name|GroupDescription
operator|.
name|Internal
condition|)
block|{
name|description
operator|=
operator|(
operator|(
name|GroupDescription
operator|.
name|Internal
operator|)
name|a
operator|)
operator|.
name|getDescription
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** @return the unique local id of the group */
DECL|method|getId ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getId
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
comment|/** @return the name of the group; null if not supplied */
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
comment|/** @return the description of the group; null if not supplied */
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

