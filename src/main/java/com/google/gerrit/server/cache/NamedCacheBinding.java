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
DECL|package|com.google.gerrit.server.cache
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/** Configure a cache declared within a {@link CacheModule} instance. */
end_comment

begin_interface
DECL|interface|NamedCacheBinding
specifier|public
interface|interface
name|NamedCacheBinding
block|{
DECL|field|INFINITE_TIME
specifier|public
specifier|static
specifier|final
name|long
name|INFINITE_TIME
init|=
literal|0L
decl_stmt|;
DECL|field|DEFAULT_TIME
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_TIME
init|=
operator|-
literal|1L
decl_stmt|;
comment|/** Set the number of objects to cache in memory. */
DECL|method|memoryLimit (int objects)
specifier|public
name|NamedCacheBinding
name|memoryLimit
parameter_list|(
name|int
name|objects
parameter_list|)
function_decl|;
comment|/** Set the number of objects to cache in memory. */
DECL|method|diskLimit (int objects)
specifier|public
name|NamedCacheBinding
name|diskLimit
parameter_list|(
name|int
name|objects
parameter_list|)
function_decl|;
comment|/** Set the time an element lives without access before being expired. */
DECL|method|timeToIdle (long duration, TimeUnit durationUnits)
specifier|public
name|NamedCacheBinding
name|timeToIdle
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|durationUnits
parameter_list|)
function_decl|;
comment|/** Set the time an element lives since creation, before being expired. */
DECL|method|timeToLive (long duration, TimeUnit durationUnits)
specifier|public
name|NamedCacheBinding
name|timeToLive
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|durationUnits
parameter_list|)
function_decl|;
comment|/** Set the eviction policy for elements when the cache is full. */
DECL|method|evictionPolicy (EvictionPolicy policy)
specifier|public
name|NamedCacheBinding
name|evictionPolicy
parameter_list|(
name|EvictionPolicy
name|policy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

