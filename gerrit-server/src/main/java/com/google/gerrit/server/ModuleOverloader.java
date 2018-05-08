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
name|inject
operator|.
name|Module
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_class
DECL|class|ModuleOverloader
specifier|public
class|class
name|ModuleOverloader
block|{
DECL|method|override (List<Module> modules, List<Module> overrideCandidates)
specifier|public
specifier|static
name|List
argument_list|<
name|Module
argument_list|>
name|override
parameter_list|(
name|List
argument_list|<
name|Module
argument_list|>
name|modules
parameter_list|,
name|List
argument_list|<
name|Module
argument_list|>
name|overrideCandidates
parameter_list|)
block|{
if|if
condition|(
name|overrideCandidates
operator|==
literal|null
operator|||
name|overrideCandidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|modules
return|;
block|}
comment|// group candidates by annotation existence
name|Map
argument_list|<
name|Boolean
argument_list|,
name|List
argument_list|<
name|Module
argument_list|>
argument_list|>
name|grouped
init|=
name|overrideCandidates
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|groupingBy
argument_list|(
name|m
lambda|->
name|m
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ModuleImpl
operator|.
name|class
argument_list|)
operator|!=
literal|null
argument_list|)
argument_list|)
decl_stmt|;
comment|// add all non annotated libs to modules list
name|List
argument_list|<
name|Module
argument_list|>
name|libs
init|=
name|grouped
operator|.
name|get
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
if|if
condition|(
name|libs
operator|!=
literal|null
condition|)
block|{
name|modules
operator|.
name|addAll
argument_list|(
name|libs
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Module
argument_list|>
name|overrides
init|=
name|grouped
operator|.
name|get
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
if|if
condition|(
name|overrides
operator|==
literal|null
condition|)
block|{
return|return
name|modules
return|;
block|}
comment|// swipe cache implementation with alternative provided in lib
return|return
name|modules
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|m
lambda|->
block|{
name|ModuleImpl
name|a
init|=
name|m
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ModuleImpl
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
return|return
name|m
return|;
block|}
return|return
name|overrides
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|o
lambda|->
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ModuleImpl
operator|.
name|class
argument_list|)
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|a
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|orElse
argument_list|(
name|m
argument_list|)
return|;
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ModuleOverloader ()
specifier|private
name|ModuleOverloader
parameter_list|()
block|{}
block|}
end_class

end_unit

