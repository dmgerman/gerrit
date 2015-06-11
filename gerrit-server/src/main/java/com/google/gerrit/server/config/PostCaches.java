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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|FLUSH_CACHES
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
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
name|cache
operator|.
name|Cache
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
name|annotations
operator|.
name|RequiresAnyCapability
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
name|AuthException
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
name|BadRequestException
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
name|Response
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
name|RestModifyView
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
name|UnprocessableEntityException
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
name|config
operator|.
name|PostCaches
operator|.
name|Input
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
name|Singleton
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
name|List
import|;
end_import

begin_class
annotation|@
name|RequiresAnyCapability
argument_list|(
block|{
name|FLUSH_CACHES
block|,
name|MAINTAIN_SERVER
block|}
argument_list|)
annotation|@
name|Singleton
DECL|class|PostCaches
specifier|public
class|class
name|PostCaches
implements|implements
name|RestModifyView
argument_list|<
name|ConfigResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|operation
specifier|public
name|Operation
name|operation
decl_stmt|;
DECL|field|caches
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|caches
decl_stmt|;
DECL|method|Input ()
specifier|public
name|Input
parameter_list|()
block|{     }
DECL|method|Input (Operation op)
specifier|public
name|Input
parameter_list|(
name|Operation
name|op
parameter_list|)
block|{
name|this
argument_list|(
name|op
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|Input (Operation op, List<String> c)
specifier|public
name|Input
parameter_list|(
name|Operation
name|op
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|c
parameter_list|)
block|{
name|operation
operator|=
name|op
expr_stmt|;
name|caches
operator|=
name|c
expr_stmt|;
block|}
block|}
DECL|enum|Operation
specifier|public
specifier|static
enum|enum
name|Operation
block|{
DECL|enumConstant|FLUSH_ALL
DECL|enumConstant|FLUSH
name|FLUSH_ALL
block|,
name|FLUSH
block|}
DECL|field|cacheMap
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cacheMap
decl_stmt|;
DECL|field|flushCache
specifier|private
specifier|final
name|FlushCache
name|flushCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|PostCaches (DynamicMap<Cache<?, ?>> cacheMap, FlushCache flushCache)
specifier|public
name|PostCaches
parameter_list|(
name|DynamicMap
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cacheMap
parameter_list|,
name|FlushCache
name|flushCache
parameter_list|)
block|{
name|this
operator|.
name|cacheMap
operator|=
name|cacheMap
expr_stmt|;
name|this
operator|.
name|flushCache
operator|=
name|flushCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource rsrc, Input input)
specifier|public
name|Object
name|apply
parameter_list|(
name|ConfigResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|BadRequestException
throws|,
name|UnprocessableEntityException
block|{
if|if
condition|(
name|input
operator|==
literal|null
operator|||
name|input
operator|.
name|operation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"operation must be specified"
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|input
operator|.
name|operation
condition|)
block|{
case|case
name|FLUSH_ALL
case|:
if|if
condition|(
name|input
operator|.
name|caches
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"specifying caches is not allowed for operation 'FLUSH_ALL'"
argument_list|)
throw|;
block|}
name|flushAll
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
literal|""
argument_list|)
return|;
case|case
name|FLUSH
case|:
if|if
condition|(
name|input
operator|.
name|caches
operator|==
literal|null
operator|||
name|input
operator|.
name|caches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"caches must be specified for operation 'FLUSH'"
argument_list|)
throw|;
block|}
name|flush
argument_list|(
name|input
operator|.
name|caches
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
literal|""
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"unsupported operation: "
operator|+
name|input
operator|.
name|operation
argument_list|)
throw|;
block|}
block|}
DECL|method|flushAll ()
specifier|private
name|void
name|flushAll
parameter_list|()
throws|throws
name|AuthException
block|{
for|for
control|(
name|DynamicMap
operator|.
name|Entry
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|e
range|:
name|cacheMap
control|)
block|{
name|CacheResource
name|cacheResource
init|=
operator|new
name|CacheResource
argument_list|(
name|e
operator|.
name|getPluginName
argument_list|()
argument_list|,
name|e
operator|.
name|getExportName
argument_list|()
argument_list|,
name|e
operator|.
name|getProvider
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|FlushCache
operator|.
name|WEB_SESSIONS
operator|.
name|equals
argument_list|(
name|cacheResource
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|flushCache
operator|.
name|apply
argument_list|(
name|cacheResource
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|flush (List<String> cacheNames)
specifier|private
name|void
name|flush
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|cacheNames
parameter_list|)
throws|throws
name|UnprocessableEntityException
throws|,
name|AuthException
block|{
name|List
argument_list|<
name|CacheResource
argument_list|>
name|cacheResources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cacheNames
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|n
range|:
name|cacheNames
control|)
block|{
name|String
name|pluginName
init|=
literal|"gerrit"
decl_stmt|;
name|String
name|cacheName
init|=
name|n
decl_stmt|;
name|int
name|i
init|=
name|cacheName
operator|.
name|lastIndexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|pluginName
operator|=
name|cacheName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|cacheName
operator|=
name|cacheName
operator|.
name|length
argument_list|()
operator|>
name|i
operator|+
literal|1
condition|?
name|cacheName
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
else|:
literal|""
expr_stmt|;
block|}
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|cache
init|=
name|cacheMap
operator|.
name|get
argument_list|(
name|pluginName
argument_list|,
name|cacheName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cacheResources
operator|.
name|add
argument_list|(
operator|new
name|CacheResource
argument_list|(
name|pluginName
argument_list|,
name|cacheName
argument_list|,
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"cache %s not found"
argument_list|,
name|n
argument_list|)
argument_list|)
throw|;
block|}
block|}
for|for
control|(
name|CacheResource
name|rsrc
range|:
name|cacheResources
control|)
block|{
name|flushCache
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

