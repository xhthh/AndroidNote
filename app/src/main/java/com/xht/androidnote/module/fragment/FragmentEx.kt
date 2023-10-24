package com.xht.androidnote.module.fragment

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * FragmentEx
 * @author Martin Hu
 * @email hy569835826@163.com
 * @data  2022/4/6 , 13:31
 * @description
 * 使用 fragment 进行构造的目的是在恢复场景 , 不需要额外创建新的 fragment  , 可直接使用
 * 当然 , 一定要确保 tag 的准确性 , 对应 viewpager adapter 1 和 2 两者的 tag 都不同 , 需要额外分析
 * 这块可以后续再处理
 *
 * 一般普通的替换, 可以考虑直接使用 switchFragment 进行加载 , 基本满足所有场景
 */
inline fun <reified T : Fragment> AppCompatActivity.fragment(
    tag: String,
    crossinline initFragment: ((String) -> Fragment?),
): T? {
    return supportFragmentManager.fragment(tag = tag, initFragment = initFragment)
}

inline fun <reified T : Fragment> Fragment.fragment(
    tag: String,
    crossinline initFragment: ((String) -> Fragment?),
): T? {

    return childFragmentManager.fragment(tag = tag, initFragment = initFragment)
}

inline fun <reified T : Fragment> FragmentManager.fragment(
    tag: String,
    crossinline initFragment: ((String) -> Fragment?),
): T? {
    val fragment: Fragment? = findFragmentByTag(tag) ?: initFragment.invoke(tag)
    return fragment as T?
}

/**
 * 显示 Fragment
 */
fun AppCompatActivity.showFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String,
) {
    supportFragmentManager.showFragment(containerId, fragment, tag)
}

/**
 * 显示 Fragment
 */
fun Fragment.showFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String,
) {
    childFragmentManager.showFragment(containerId, fragment, tag)
}


/**
 *  显示 Fragment
 */
fun FragmentManager.showFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String,
) {
    switchFragment(containerId = containerId, tag = tag, initFragment = { fragment })
}


/**
 * 隐藏 Fragment
 */
fun AppCompatActivity.replaceFragment(
    @IdRes containerId: Int,
    tag: String,
) {
    supportFragmentManager.dissMissFragment(containerId, tag)
}

/**
 * 隐藏 Fragment
 */
fun Fragment.dissMissFragment(
    @IdRes containerId: Int,
    tag: String,
) {
    childFragmentManager.dissMissFragment(containerId, tag)
}


/**
 *  隐藏 Fragment
 */
fun FragmentManager.dissMissFragment(
    @IdRes containerId: Int,
    tag: String,
) {
    val ft: FragmentTransaction = beginTransaction()
    findFragmentByTag(tag)?.let {
        if (it.id == containerId && it.tag != tag && !it.isDetached) {
            ft.detach(it)
        }
    }
}

/**
 * 替换Fragment
 */
fun FragmentManager.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String? = null,
) {
    if (fragment.isAdded) return
    val realTag = tag ?: fragment.javaClass.name
    beginTransaction().also {
        it.replace(containerId, fragment, realTag)
        it.commitNowAllowingStateLoss()
    }
}

/**
 * 替换Fragment
 */
fun AppCompatActivity.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String? = null,
) {
    supportFragmentManager.replaceFragment(containerId, fragment, tag)
}

/**
 * 替换Fragment
 */
fun Fragment.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    tag: String? = null,
) {
    childFragmentManager.replaceFragment(containerId, fragment, tag)
}


/**
 * 移除Fragment
 */
fun AppCompatActivity.removeFragment(
    fragment: Fragment?,
) {
    fragment?.let {
        if (fragment.isRemoving) return
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.remove(fragment)
        beginTransaction.commitNowAllowingStateLoss()
    }
}

/**
 * 移除Fragment
 */
fun Fragment.removeFragment(
    fragment: Fragment?,
) {
    fragment?.let {
        if (fragment.isRemoving) return
        val beginTransaction = childFragmentManager.beginTransaction()
        beginTransaction.remove(fragment)
        beginTransaction.commitNowAllowingStateLoss()
    }
}

/**
 * 切换Fragment，按需加载
 * @return 第一次添加调用invoke生成Fragment添加，此时返回null。如果Fragment已经添加过，返回被重新显示Fragment
 */
inline fun AppCompatActivity.switchFragment(
    @IdRes containerId: Int,
    tag: String,
    crossinline initFragment: ((tag: String) -> Fragment),
): Fragment {
    return supportFragmentManager.switchFragment(
        containerId = containerId,
        tag = tag,
        initFragment = initFragment
    )
}

inline fun FragmentActivity.switchFragment(
    @IdRes containerId: Int,
    tag: String,
    crossinline initFragment: ((tag: String) -> Fragment),
): Fragment {
    return supportFragmentManager.switchFragment(
        containerId = containerId,
        tag = tag,
        initFragment = initFragment
    )
}

/**
 * 切换Fragment，按需加载
 * @return 第一次添加调用invoke生成Fragment添加，此时返回null。如果Fragment已经添加过，返回被重新显示Fragment
 */
inline fun Fragment.switchFragment(
    @IdRes containerId: Int,
    tag: String,
    crossinline initFragment: ((tag: String) -> Fragment),
): Fragment {

    return childFragmentManager.switchFragment(
        containerId = containerId,
        tag = tag,
        initFragment = initFragment
    )
}

/**
 * 切换Fragment，按需加载
 * @return 第一次添加调用invoke生成Fragment添加，此时返回null。如果Fragment已经添加过，返回被重新显示Fragment
 */
inline fun FragmentManager.switchFragment(
    @IdRes containerId: Int,
    tag: String,
    crossinline initFragment: ((tag: String) -> Fragment),
): Fragment {
    val ft: FragmentTransaction = beginTransaction()
    val findFragment = findFragmentByTag(tag)?.also {
        if (it.isDetached) {
            ft.attach(it)
        }
    } ?: kotlin.run {
        val temp = initFragment.invoke(tag)
        ft.add(containerId, temp, tag)
        temp
    }
    fragments.forEach {
        if (it.id == containerId && it.tag != tag && !it.isDetached) {
            ft.detach(it)
        }
    }
    ft.commitNowAllowingStateLoss()
    return findFragment
}