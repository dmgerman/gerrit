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

begin_class
DECL|class|Main
specifier|public
specifier|final
class|class
name|Main
block|{
comment|// We don't do any real work here because we need to import
comment|// the archive lookup code and we cannot import a class in
comment|// the default package. So this is just a tiny springboard
comment|// to jump into the real main code.
comment|//
DECL|method|main (final String[] argv)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|onSupportedJavaVersion
argument_list|()
condition|)
block|{
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|launcher
operator|.
name|GerritLauncher
operator|.
name|main
argument_list|(
name|argv
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onSupportedJavaVersion ()
specifier|private
specifier|static
name|boolean
name|onSupportedJavaVersion
parameter_list|()
block|{
specifier|final
name|String
name|version
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|1.8
operator|<=
name|parse
argument_list|(
name|version
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"fatal: Gerrit Code Review requires Java 8 or later"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"       (trying to run on Java "
operator|+
name|version
operator|+
literal|")"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
DECL|method|parse (String version)
specifier|private
specifier|static
name|double
name|parse
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|0.0
return|;
block|}
try|try
block|{
specifier|final
name|int
name|fd
init|=
name|version
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
specifier|final
name|int
name|sd
init|=
name|version
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|fd
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|sd
condition|)
block|{
name|version
operator|=
name|version
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sd
argument_list|)
expr_stmt|;
block|}
return|return
name|Double
operator|.
name|parseDouble
argument_list|(
name|version
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
literal|0.0
return|;
block|}
block|}
DECL|method|Main ()
specifier|private
name|Main
parameter_list|()
block|{   }
block|}
end_class

end_unit

