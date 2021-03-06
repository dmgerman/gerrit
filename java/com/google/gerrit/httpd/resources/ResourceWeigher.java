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
DECL|package|com.google.gerrit.httpd.resources
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|resources
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
name|cache
operator|.
name|Weigher
import|;
end_import

begin_class
DECL|class|ResourceWeigher
specifier|public
class|class
name|ResourceWeigher
implements|implements
name|Weigher
argument_list|<
name|ResourceKey
argument_list|,
name|Resource
argument_list|>
block|{
annotation|@
name|Override
DECL|method|weigh (ResourceKey key, Resource value)
specifier|public
name|int
name|weigh
parameter_list|(
name|ResourceKey
name|key
parameter_list|,
name|Resource
name|value
parameter_list|)
block|{
return|return
name|key
operator|.
name|weigh
argument_list|()
operator|+
name|value
operator|.
name|weigh
argument_list|()
return|;
block|}
block|}
end_class

end_unit

