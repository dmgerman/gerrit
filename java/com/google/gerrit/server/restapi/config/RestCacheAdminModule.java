begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|CacheResource
operator|.
name|CACHE_KIND
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigResource
operator|.
name|CONFIG_KIND
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
name|RestApiModule
import|;
end_import

begin_class
DECL|class|RestCacheAdminModule
specifier|public
class|class
name|RestCacheAdminModule
extends|extends
name|RestApiModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|CACHE_KIND
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CONFIG_KIND
argument_list|,
literal|"caches"
argument_list|)
operator|.
name|to
argument_list|(
name|CachesCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|postOnCollection
argument_list|(
name|CACHE_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|PostCaches
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CACHE_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetCache
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CACHE_KIND
argument_list|,
literal|"flush"
argument_list|)
operator|.
name|to
argument_list|(
name|FlushCache
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CONFIG_KIND
argument_list|,
literal|"summary"
argument_list|)
operator|.
name|to
argument_list|(
name|GetSummary
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

