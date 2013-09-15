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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|base
operator|.
name|Splitter
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
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
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

begin_class
DECL|class|ConfigAnnotationParser
class|class
name|ConfigAnnotationParser
block|{
DECL|field|splitter
specifier|private
specifier|static
name|Splitter
name|splitter
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|"."
argument_list|)
operator|.
name|trimResults
argument_list|()
decl_stmt|;
DECL|method|parse (GerritConfigs annotation)
specifier|static
name|Config
name|parse
parameter_list|(
name|GerritConfigs
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
for|for
control|(
name|GerritConfig
name|c
range|:
name|annotation
operator|.
name|value
argument_list|()
control|)
block|{
name|parse
argument_list|(
name|cfg
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|cfg
return|;
block|}
DECL|method|parse (GerritConfig annotation)
specifier|static
name|Config
name|parse
parameter_list|(
name|GerritConfig
name|annotation
parameter_list|)
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|parse
argument_list|(
name|cfg
argument_list|,
name|annotation
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|method|parse (Config cfg, GerritConfig c)
specifier|static
specifier|private
name|void
name|parse
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|GerritConfig
name|c
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|l
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|splitter
operator|.
name|split
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|null
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|c
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|l
operator|.
name|size
argument_list|()
operator|==
literal|3
condition|)
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|c
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"GerritConfig.name must be of the format"
operator|+
literal|" section.subsection.name or section.name"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

