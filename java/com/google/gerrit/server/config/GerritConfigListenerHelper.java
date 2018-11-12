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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_class
DECL|class|GerritConfigListenerHelper
specifier|public
class|class
name|GerritConfigListenerHelper
block|{
DECL|method|acceptIfChanged (ConfigKey... keys)
specifier|public
specifier|static
name|GerritConfigListener
name|acceptIfChanged
parameter_list|(
name|ConfigKey
modifier|...
name|keys
parameter_list|)
block|{
return|return
name|e
lambda|->
name|e
operator|.
name|isEntriesUpdated
argument_list|(
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|keys
argument_list|)
argument_list|)
condition|?
name|e
operator|.
name|accept
argument_list|(
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|keys
argument_list|)
argument_list|)
else|:
name|ConfigUpdatedEvent
operator|.
name|NO_UPDATES
return|;
block|}
block|}
end_class

end_unit

