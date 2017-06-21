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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|account
operator|.
name|GroupMembership
import|;
end_import

begin_comment
comment|/**  * User identity for plugin code that needs an identity.  *  *<p>An InternalUser has no real identity, it acts as the server and can access anything it wants,  * anytime it wants, given the JVM's own direct access to data. Plugins may use this when they need  * to have a CurrentUser with read permission on anything.  *  * @see PluginUser  */
end_comment

begin_class
DECL|class|InternalUser
specifier|public
class|class
name|InternalUser
extends|extends
name|CurrentUser
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|InternalUser
name|create
parameter_list|()
function_decl|;
block|}
annotation|@
name|Override
DECL|method|getEffectiveGroups ()
specifier|public
name|GroupMembership
name|getEffectiveGroups
parameter_list|()
block|{
return|return
name|GroupMembership
operator|.
name|EMPTY
return|;
block|}
annotation|@
name|Override
DECL|method|isInternalUser ()
specifier|public
name|boolean
name|isInternalUser
parameter_list|()
block|{
return|return
literal|true
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
literal|"InternalUser"
return|;
block|}
block|}
end_class

end_unit

