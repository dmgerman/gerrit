begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
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

begin_class
DECL|class|CacheControl
specifier|public
class|class
name|CacheControl
block|{
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|NONE
DECL|enumConstant|PUBLIC
DECL|enumConstant|PRIVATE
name|NONE
block|,
name|PUBLIC
block|,
name|PRIVATE
block|;   }
DECL|field|NONE
specifier|public
specifier|final
specifier|static
name|CacheControl
name|NONE
init|=
operator|new
name|CacheControl
argument_list|(
name|Type
operator|.
name|NONE
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|method|PUBLIC (long age, TimeUnit unit)
specifier|public
specifier|static
name|CacheControl
name|PUBLIC
parameter_list|(
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|new
name|CacheControl
argument_list|(
name|Type
operator|.
name|PUBLIC
argument_list|,
name|age
argument_list|,
name|unit
argument_list|)
return|;
block|}
DECL|method|PRIVATE (long age, TimeUnit unit)
specifier|public
specifier|static
name|CacheControl
name|PRIVATE
parameter_list|(
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|new
name|CacheControl
argument_list|(
name|Type
operator|.
name|PRIVATE
argument_list|,
name|age
argument_list|,
name|unit
argument_list|)
return|;
block|}
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
DECL|field|age
specifier|private
specifier|final
name|long
name|age
decl_stmt|;
DECL|field|unit
specifier|private
specifier|final
name|TimeUnit
name|unit
decl_stmt|;
DECL|field|mustRevalidate
specifier|private
name|boolean
name|mustRevalidate
decl_stmt|;
DECL|method|CacheControl (Type type, long age, TimeUnit unit)
specifier|private
name|CacheControl
parameter_list|(
name|Type
name|type
parameter_list|,
name|long
name|age
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|age
operator|=
name|age
expr_stmt|;
name|this
operator|.
name|unit
operator|=
name|unit
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getAge ()
specifier|public
name|long
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
DECL|method|getUnit ()
specifier|public
name|TimeUnit
name|getUnit
parameter_list|()
block|{
return|return
name|unit
return|;
block|}
DECL|method|isMustRevalidate ()
specifier|public
name|boolean
name|isMustRevalidate
parameter_list|()
block|{
return|return
name|mustRevalidate
return|;
block|}
DECL|method|setMustRevalidate ()
specifier|public
name|CacheControl
name|setMustRevalidate
parameter_list|()
block|{
name|mustRevalidate
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

