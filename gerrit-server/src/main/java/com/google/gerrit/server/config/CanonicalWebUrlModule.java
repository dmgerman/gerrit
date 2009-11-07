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
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Scopes
operator|.
name|SINGLETON
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
name|AbstractModule
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

begin_comment
comment|/** Supports binding the {@link CanonicalWebUrl} annotation. */
end_comment

begin_class
DECL|class|CanonicalWebUrlModule
specifier|public
specifier|abstract
class|class
name|CanonicalWebUrlModule
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
comment|// Note that the CanonicalWebUrl itself must not be a singleton, but its
comment|// provider must be.
comment|//
comment|// If the value was not configured in the system configuration data the
comment|// provider may try to guess it from the current HTTP request, if we are
comment|// running in an HTTP environment.
comment|//
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|String
argument_list|>
argument_list|>
name|provider
init|=
name|provider
argument_list|()
decl_stmt|;
name|bind
argument_list|(
name|provider
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|CanonicalWebUrl
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
DECL|method|provider ()
specifier|protected
specifier|abstract
name|Class
argument_list|<
name|?
extends|extends
name|Provider
argument_list|<
name|String
argument_list|>
argument_list|>
name|provider
parameter_list|()
function_decl|;
block|}
end_class

end_unit

