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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|extensions
operator|.
name|restapi
operator|.
name|ChildCollection
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|AccountResource
operator|.
name|Capability
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
name|Provider
import|;
end_import

begin_class
DECL|class|Capabilities
class|class
name|Capabilities
implements|implements
name|ChildCollection
argument_list|<
name|AccountResource
argument_list|,
name|AccountResource
operator|.
name|Capability
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|Capability
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|get
specifier|private
specifier|final
name|Provider
argument_list|<
name|GetCapabilities
argument_list|>
name|get
decl_stmt|;
annotation|@
name|Inject
DECL|method|Capabilities ( DynamicMap<RestView<AccountResource.Capability>> views, Provider<GetCapabilities> get)
name|Capabilities
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|AccountResource
operator|.
name|Capability
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|GetCapabilities
argument_list|>
name|get
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|get
operator|=
name|get
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|GetCapabilities
name|list
parameter_list|()
throws|throws
name|ResourceNotFoundException
block|{
return|return
name|get
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parse (AccountResource parent, String id)
specifier|public
name|Capability
name|parse
parameter_list|(
name|AccountResource
name|parent
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|Exception
block|{
name|CapabilityControl
name|cap
init|=
name|parent
operator|.
name|getUser
argument_list|()
operator|.
name|getCapabilities
argument_list|()
decl_stmt|;
if|if
condition|(
name|cap
operator|.
name|canPerform
argument_list|(
name|id
argument_list|)
operator|||
operator|(
name|cap
operator|.
name|canAdministrateServer
argument_list|()
operator|&&
name|GlobalCapability
operator|.
name|isCapability
argument_list|(
name|id
argument_list|)
operator|)
condition|)
block|{
return|return
operator|new
name|AccountResource
operator|.
name|Capability
argument_list|(
name|parent
operator|.
name|getUser
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
block|}
end_class

end_unit

